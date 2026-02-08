/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 NaturalModels
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit

import com.vdurmont.semver4j.Semver
import id.naturalsmp.naturalmodels.api.NaturalModelsConfig
import id.naturalsmp.naturalmodels.api.NaturalModelsEvaluator
import id.naturalsmp.naturalmodels.api.NaturalModelsLogger
import id.naturalsmp.naturalmodels.api.NaturalModelsPlatform.ReloadResult
import id.naturalsmp.naturalmodels.api.NaturalModelsPlatform.ReloadResult.*
import id.naturalsmp.naturalmodels.api.bukkit.BukkitModelEventBus
import id.naturalsmp.naturalmodels.api.bukkit.scheduler.BukkitModelScheduler
import id.naturalsmp.naturalmodels.api.manager.*
import id.naturalsmp.naturalmodels.api.nms.NMS
import id.naturalsmp.naturalmodels.api.pack.PackZipper
import id.naturalsmp.naturalmodels.api.version.MinecraftVersion
import id.naturalsmp.naturalmodels.bukkit.command.startBukkitCommand
import id.naturalsmp.naturalmodels.bukkit.configuration.PluginConfiguration
import id.naturalsmp.naturalmodels.bukkit.manager.PlayerManagerImpl
import id.naturalsmp.naturalmodels.bukkit.util.ADVENTURE_PLATFORM
import id.naturalsmp.naturalmodels.bukkit.util.audience
import id.naturalsmp.naturalmodels.bukkit.util.registerListener
import id.naturalsmp.naturalmodels.manager.*
import id.naturalsmp.naturalmodels.util.*
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.format.NamedTextColor.*
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.server.ServerLoadEvent
import java.io.File
import java.io.InputStream
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.jar.JarEntry
import java.util.jar.JarFile

abstract class NaturalModelsPlugin : AbstractNaturalModelsPlugin() {

    private lateinit var props: NaturalModelsProperties

    override fun onLoad() {
        super.onLoad()
        props = runCatching {
            NaturalModelsProperties(this)
        }.getOrElse {
            warn(
                "Unable to start NaturalModels.".toComponent(),
                "Reason: ${it.message ?: "Unknown"}".toComponent(RED),
                "Stack trace: ${it.stackTraceToString()}".toComponent(RED),
                "Plugin will be automatically disabled.".toComponent(DARK_RED)
            )
            return Bukkit.getPluginManager().disablePlugin(this)
        }
    }

    override fun onEnable() {
        props.managers.forEach(GlobalManager::start)
        if (isSnapshot) warn(
            "This build is dev version: be careful to use it!".toComponent(),
            "Build number: ${props.snapshot}".toComponent(LIGHT_PURPLE)
        )
        startBukkitCommand()
        registerListener(object : Listener {
            @EventHandler
            fun PlayerJoinEvent.join() {
                if (!player.isOp || !config().versionCheck()) return
                props.scheduler.asyncTask {
                    val result = LATEST_VERSION
                    player.audience().infoNotNull(
                        result.release
                            ?.takeIf { props.semver < it.versionNumber() }
                            ?.let { version -> componentOf("New NaturalModels release found: ") { append(version.toURLComponent()) } },
                        result.snapshot
                            ?.takeIf { props.semver < it.versionNumber() }
                            ?.let { version -> componentOf("New NaturalModels snapshot found: ") { append(version.toURLComponent()) } }
                    )
                }
            }

            @EventHandler
            fun ServerLoadEvent.load() {
                if (skipInitialReload || type != ServerLoadEvent.LoadType.STARTUP) return
                when (val result = reload(ReloadInfo(true, Audience.empty()))) {
                    is Failure -> result.throwable.handleException("Unable to load plugin properly.")
                    is OnReload -> throw RuntimeException("Plugin load failed.")
                    is Success -> info(
                        "Plugin is loaded. (${result.totalTime().withComma()} ms)".toComponent(GREEN),
                        "Minecraft version: ${props.version}, NMS version: ${props.nms.version()}".toComponent(AQUA),
                        "Platform: ${
                            when {
                                IS_FOLIA -> "Folia"
                                IS_PURPUR -> "Purpur"
                                IS_PAPER -> "Paper"
                                else -> "Bukkit"
                            }
                        }".toComponent(AQUA)
                    )
                }
            }
        })
    }

    override fun onDisable() {
        if (!firstLoad.get()) return
        props.managers.forEach(GlobalManager::end)
        ADVENTURE_PLATFORM?.close()
    }

    override fun reload(info: ReloadInfo): ReloadResult {
        if (!onReload.compareAndSet(false, true)) return OnReload.INSTANCE
        return runCatching {
            if (!info.skipConfig) props.config = NaturalModelsConfigImpl(PluginConfiguration.CONFIG.create())
            val zipper = PackZipper.zipper().also(props.reloadStartTask)
            ReloadPipeline(
                config().indicator().options.toIndicator(info)
            ).use { pipeline ->
                val time = System.currentTimeMillis()
                props.managers.forEach {
                    it.reload(pipeline, zipper)
                }
                Success(
                    firstLoad.compareAndSet(false, true),
                    System.currentTimeMillis() - time,
                    config().packType().toGenerator().create(zipper, pipeline.apply {
                        status = "Generating files..."
                        goal = zipper.size()
                    })
                )
            }
        }.getOrElse {
            Failure(it)
        }.apply {
            onReload.set(false)
        }.also(props.reloadEndTask)
    }

    override fun loadAssets(pipeline: ReloadPipeline, prefix: String, consumer: BiConsumer<String, InputStream>) {
        JarFile(file).use {
            pipeline.forEachParallel(it.entries()
                .asSequence()
                .filter { entry ->
                    entry.name.startsWith(prefix)
                        && entry.name.length > prefix.length + 1
                        && !entry.isDirectory
                }
                .toList(),
                JarEntry::getSize
            ) { entry ->
                it.getInputStream(entry).use { stream ->
                    consumer.accept(entry.name.substring(prefix.length + 1), stream)
                }
            }
        }
    }

    override fun dataFolder(): File = dataFolder
    override fun logger(): NaturalModelsLogger = logger
    override fun scheduler(): BukkitModelScheduler = props.scheduler
    override fun evaluator(): NaturalModelsEvaluator = props.evaluator
    override fun eventBus(): BukkitModelEventBus = props.eventbus
    override fun modelManager(): ModelManager = ModelManagerImpl
    override fun playerManager(): PlayerManager = PlayerManagerImpl
    override fun scriptManager(): ScriptManager = ScriptManagerImpl
    override fun skinManager(): SkinManager = SkinManagerImpl
    override fun profileManager(): ProfileManager = ProfileManagerImpl

    override fun config(): NaturalModelsConfig = props.config
    override fun version(): MinecraftVersion = props.version
    override fun semver(): Semver = props.semver
    override fun nms(): NMS = props.nms
    override fun isSnapshot(): Boolean = props.snapshot > 0

    @Synchronized
    override fun addReloadStartHandler(consumer: Consumer<PackZipper>) {
        val previous = props.reloadStartTask
        props.reloadStartTask = {
            previous(it)
            consumer.accept(it)
        }
    }

    @Synchronized
    override fun addReloadEndHandler(consumer: Consumer<ReloadResult>) {
        val previous = props.reloadEndTask
        props.reloadEndTask = {
            previous(it)
            consumer.accept(it)
        }
    }
}


