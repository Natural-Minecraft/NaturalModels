/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.impl.fabric

import com.vdurmont.semver4j.Semver
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils
import eu.pb4.polymer.resourcepack.api.ResourcePackBuilder
import id.naturalsmp.naturalmodels.NaturalModelsEvaluatorImpl
import id.naturalsmp.naturalmodels.NaturalModelsEventBusImpl
import id.naturalsmp.naturalmodels.NaturalModelsPlatformImpl
import id.naturalsmp.naturalmodels.api.*
import id.naturalsmp.naturalmodels.api.NaturalModelsPlatform.ReloadResult.*
import id.naturalsmp.naturalmodels.api.event.PluginEndReloadEvent
import id.naturalsmp.naturalmodels.api.event.PluginStartReloadEvent
import id.naturalsmp.naturalmodels.api.fabric.NaturalModelsFabric
import id.naturalsmp.naturalmodels.api.fabric.platform.FabricAdapter
import id.naturalsmp.naturalmodels.api.fabric.scheduler.FabricModelScheduler
import id.naturalsmp.naturalmodels.api.manager.*
import id.naturalsmp.naturalmodels.api.nms.NMS
import id.naturalsmp.naturalmodels.api.pack.PackResult
import id.naturalsmp.naturalmodels.api.pack.PackZipper
import id.naturalsmp.naturalmodels.api.platform.PlatformAdapter
import id.naturalsmp.naturalmodels.api.version.MinecraftVersion
import id.naturalsmp.naturalmodels.impl.fabric.attachment.NaturalModelsAttachments
import id.naturalsmp.naturalmodels.impl.fabric.command.startFabricCommand
import id.naturalsmp.naturalmodels.impl.fabric.config.NaturalModelsConfigImpl
import id.naturalsmp.naturalmodels.impl.fabric.config.toConfig
import id.naturalsmp.naturalmodels.impl.fabric.manager.EntityManager
import id.naturalsmp.naturalmodels.impl.fabric.manager.PlayerManagerImpl
import id.naturalsmp.naturalmodels.impl.fabric.scheduler.FabricModelSchedulerImpl
import id.naturalsmp.naturalmodels.manager.*
import id.naturalsmp.naturalmodels.util.*
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.loader.api.FabricLoader
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.format.NamedTextColor.AQUA
import net.kyori.adventure.text.format.NamedTextColor.GREEN
import net.minecraft.DetectedVersion
import net.minecraft.WorldVersion
import net.minecraft.server.MinecraftServer
import net.minecraft.server.packs.metadata.pack.PackFormat
import net.minecraft.util.InclusiveRange
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.jar.JarFile
import kotlin.io.path.exists
import kotlin.system.measureTimeMillis

class NaturalModelsFabricImpl : ModInitializer, NaturalModelsPlatformImpl, NaturalModelsFabric {
    private lateinit var server: MinecraftServer

    private val configDir: Path = FabricLoader.getInstance()
        .configDir
        .resolve(modId()).apply {
            toFile().mkdirs()
        }

    private val jarFile: JarFile
        get() = JarFile(
            File(javaClass.getProtectionDomain().codeSource.location.toURI())
        )

    private lateinit var config: NaturalModelsConfigImpl

    private val worldVersion: WorldVersion = DetectedVersion.tryDetectVersion()
    private val minecraftVersion: MinecraftVersion = MinecraftVersion.parse(worldVersion.id())

    private val semver: Semver = FabricLoader.getInstance()
        .getModContainer(modId())
        .map { modContainer ->
            Semver(
                modContainer.metadata.version.friendlyString,
                Semver.SemverType.LOOSE
            )
        }
        .orElseThrow()

    private val nms by lazy {
        NaturalModelsNMSImpl()
    }
    private val logger = NaturalModelsLoggerImpl()

    private var reloadStartTask: (PackZipper) -> Unit = { zipper ->
        callEvent {
            PluginStartReloadEvent(zipper)
        }
    }

    private var reloadEndTask: (NaturalModelsPlatform.ReloadResult) -> Unit = { result ->
        callEvent {
            PluginEndReloadEvent(result)
        }
    }

    private val isLoadingProvider: AtomicBoolean = AtomicBoolean()
    private val isFirstLoadProvider: AtomicBoolean = AtomicBoolean()

    private val allManagers by lazy {
        listOf(
            ArmorManager,
            ProfileManagerImpl,
            SkinManagerImpl,
            ModelManagerImpl,
            PlayerManagerImpl,
            EntityManager,
            ScriptManagerImpl
        )
    }

    override fun onInitialize() {
        NaturalModels.register(this)
        startFabricCommand()
        ServerLifecycleEvents.SERVER_STARTING.register { server ->
            this.server = server
        }

        PolymerResourcePackUtils.addModAssets(modId())
        PolymerResourcePackUtils.markAsRequired()

        config = loadOrSaveConfig()

        val initialLoad = AtomicBoolean()

        PolymerResourcePackUtils.RESOURCE_PACK_CREATION_EVENT.register { builder ->
            val isInitialLoad = initialLoad.compareAndSet(false, true)
            reload {
                includeAssets(builder, it.packResult)
                if (isInitialLoad) loadLog(it)
            }
        }

        ServerLifecycleEvents.SERVER_STARTED.register {
            allManagers.forEach {
                it.start()
            }
            if (initialLoad.compareAndSet(false, true)) reload { loadLog(it) }
        }

        NaturalModelsAttachments.init()
        FabricModelSchedulerImpl.init()

        ServerLifecycleEvents.SERVER_STOPPED.register {
            allManagers.forEach { manager ->
                manager.end()
            }
        }
    }

    private fun reload(callback: (Success) -> Unit) {
        when (val result = reload(ReloadInfo(true, Audience.empty()))) {
            is Failure -> result.throwable.handleException("Unable to load mod properly.")
            is OnReload -> throw RuntimeException("mod load failed.")
            is Success -> callback(result)
        }
    }

    private fun includeAssets(builder: ResourcePackBuilder, packResult: PackResult) {
        packResult.stream().forEach { packByte ->
            when (val path = packByte.path.path) {
                "pack.png", "pack.mcmeta" -> return@forEach
                else ->  builder.addData(path, packByte.bytes)
            }
        }
        packResult.meta().overlays?.entries?.forEach { entry ->
            if (entry.directory == "NaturalModels_legacy") {
                return@forEach
            }

            val min = entry.minFormat.run { PackFormat(major, minor) }
            val max = entry.maxFormat.run { PackFormat(major, minor) }
            val range = InclusiveRange(min, max)

            builder.packMcMetaBuilder.addOverlay(range, entry.directory)
        }
    }

    private fun loadLog(success: Success) {
        info(
            "Mod is loaded. (${success.totalTime().withComma()} ms)".toComponent(GREEN),
            "Platform: Fabric".toComponent(AQUA)
        )
    }

    override fun getResource(fileName: String): InputStream? {
        return javaClass.getResourceAsStream("/$fileName")
    }

    override fun saveResource(fileName: String) {
        getResource(fileName)?.use { input ->
            Files.copy(input, configDir.resolve(fileName))
        }
    }

    override fun loadAssets(pipeline: ReloadPipeline, prefix: String, consumer: BiConsumer<String, InputStream>) {
        jarFile.use { jarFile ->
            val jarEntries = jarFile.entries()
                .asSequence()
                .filter { jarEntry ->
                    jarEntry.name.startsWith(prefix) &&
                        jarEntry.name.length > prefix.length + 1 &&
                        !jarEntry.isDirectory
                }
                .toList()

            pipeline.forEachParallel(
                jarEntries,
                { it.size }
            ) { jarEntry ->
                jarFile.getInputStream(jarEntry).use { stream ->
                    consumer.accept(jarEntry.name.substring(prefix.length + 1), stream)
                }
            }
        }
    }

    override fun dataFolder(): File = configDir.toFile()

    override fun jarType(): NaturalModelsPlatform.JarType = NaturalModelsPlatform.JarType.FABRIC

    override fun reload(info: ReloadInfo): NaturalModelsPlatform.ReloadResult {
        if (!isLoadingProvider.compareAndSet(false, true)) {
            return OnReload.INSTANCE
        }

        return runCatching {
            if (!info.skipConfig) {
                config = loadOrSaveConfig()
            }

            val zipper = PackZipper.zipper()
            reloadStartTask(zipper)

            val indicators = config().indicator().options.toIndicator(info)
            ReloadPipeline(indicators).use { pipeline ->
                val assetsTime = measureTimeMillis {
                    allManagers.forEach { manager ->
                        manager.reload(pipeline, zipper)
                    }
                }

                pipeline.run {
                    status = "Generating files..."
                    goal = zipper.size()
                }

                val isFirstLoad = isFirstLoadProvider.compareAndSet(false, true)
                val packResult = config().packType().toGenerator().create(zipper, pipeline)

                Success(
                    isFirstLoad,
                    assetsTime,
                    packResult
                )
            }

        }
            .getOrElse { throwable ->
                Failure(throwable)
            }
            .also { result ->
                isLoadingProvider.set(false)
                reloadEndTask(result)
            }
    }

    private fun loadOrSaveConfig(): NaturalModelsConfigImpl {
        return configDir.resolve("config.yml").run {
            if (!exists()) saveResource("config.yml")
            toConfig()
        }
    }

    override fun isSnapshot(): Boolean = !worldVersion.stable()

    override fun config(): NaturalModelsConfig = config

    override fun version(): MinecraftVersion = minecraftVersion

    override fun semver(): Semver = semver

    override fun nms(): NMS = nms

    override fun modelManager(): ModelManager = ModelManagerImpl

    override fun playerManager(): PlayerManager = PlayerManagerImpl

    override fun scriptManager(): ScriptManager = ScriptManagerImpl

    override fun skinManager(): SkinManager = SkinManagerImpl

    override fun profileManager(): ProfileManager = ProfileManagerImpl

    override fun addReloadStartHandler(consumer: Consumer<PackZipper>) {
        val oldHandler = reloadStartTask
        reloadStartTask = { zipper ->
            oldHandler(zipper)
            consumer.accept(zipper)
        }
    }

    override fun addReloadEndHandler(consumer: Consumer<NaturalModelsPlatform.ReloadResult>) {
        val oldHandler = reloadEndTask
        reloadEndTask = { result ->
            oldHandler(result)
            consumer.accept(result)
        }
    }

    override fun logger(): NaturalModelsLogger = logger

    override fun evaluator(): NaturalModelsEvaluator = NaturalModelsEvaluatorImpl()

    override fun eventBus(): NaturalModelsEventBus = NaturalModelsEventBusImpl()

    override fun server(): MinecraftServer = server

    override fun scheduler(): FabricModelScheduler = FabricModelSchedulerImpl

    override fun adapter(): PlatformAdapter = FabricAdapter()

    override fun isEnabled(): Boolean = true
}


