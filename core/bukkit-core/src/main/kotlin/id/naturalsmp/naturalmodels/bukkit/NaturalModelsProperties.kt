/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit

import com.vdurmont.semver4j.Semver
import id.naturalsmp.naturalmodels.NaturalModelsEvaluatorImpl
import id.naturalsmp.naturalmodels.api.NaturalModelsConfig
import id.naturalsmp.naturalmodels.api.NaturalModelsPlatform.ReloadResult
import id.naturalsmp.naturalmodels.api.bukkit.NaturalModelsBukkit
import id.naturalsmp.naturalmodels.api.event.PluginEndReloadEvent
import id.naturalsmp.naturalmodels.api.event.PluginStartReloadEvent
import id.naturalsmp.naturalmodels.api.pack.PackZipper
import id.naturalsmp.naturalmodels.api.version.MinecraftVersion.*
import id.naturalsmp.naturalmodels.bukkit.configuration.PluginConfiguration
import id.naturalsmp.naturalmodels.bukkit.manager.CompatibilityManager
import id.naturalsmp.naturalmodels.bukkit.manager.EntityManager
import id.naturalsmp.naturalmodels.bukkit.manager.PlayerManagerImpl
import id.naturalsmp.naturalmodels.bukkit.scheduler.BukkitScheduler
import id.naturalsmp.naturalmodels.bukkit.scheduler.PaperScheduler
import id.naturalsmp.naturalmodels.manager.*
import id.naturalsmp.naturalmodels.util.callEvent
import id.naturalsmp.naturalmodels.util.handleException
import id.naturalsmp.naturalmodels.util.toComponent
import id.naturalsmp.naturalmodels.util.warn
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit

private typealias Latest = id.naturalsmp.naturalmodels.bukkit.nms.v1_21_R7.NMSImpl

internal class NaturalModelsProperties(
    private val plugin: AbstractNaturalModelsPlugin
) {
    private lateinit var _config: NaturalModelsConfig
    private var _metrics: Metrics? = null

    val version = parse(Bukkit.getBukkitVersion().substringBefore('-'))
    val nms = when (version) {
        V1_21_11 -> Latest()
        V1_21_9, V1_21_10 -> id.naturalsmp.naturalmodels.bukkit.nms.v1_21_R6.NMSImpl()
        V1_21_6, V1_21_7, V1_21_8 -> id.naturalsmp.naturalmodels.bukkit.nms.v1_21_R5.NMSImpl()
        V1_21_5 -> id.naturalsmp.naturalmodels.bukkit.nms.v1_21_R4.NMSImpl()
        V1_21_4 -> id.naturalsmp.naturalmodels.bukkit.nms.v1_21_R3.NMSImpl()
        V1_21, V1_21_1 -> id.naturalsmp.naturalmodels.bukkit.nms.v1_21_R1.NMSImpl()
        else if NaturalModelsBukkit.IS_PAPER -> {
            warn(
                "Note: this version is officially untested.".toComponent(),
                "So be careful to use!".toComponent()
            )
            Latest()
        }
        else -> throw RuntimeException("Unsupported version: $version")
    }
    val scheduler = if (NaturalModelsBukkit.IS_FOLIA) PaperScheduler() else BukkitScheduler()
    val evaluator = NaturalModelsEvaluatorImpl()
    val eventbus = BukkitModelEventBusImpl()
    @Suppress("DEPRECATION") //To support Spigot :(
    val semver = Semver(plugin.description.version, Semver.SemverType.LOOSE)
    val snapshot = runCatching {
        plugin.attributes().getValue("Dev-Build").toInt()
    }.getOrElse {
        it.handleException("Unable to parse manifest's build data")
        -1
    }
    var config
        get() = _config
        set(value) {
            _config = value.apply {
                if (metrics()) {
                    if (_metrics == null) _metrics = Metrics(plugin, 24237)
                } else {
                    _metrics?.shutdown()
                    _metrics = null
                }
            }
        }
    val managers by lazy {
        listOf(
            CompatibilityManager,
            ArmorManager,
            ProfileManagerImpl,
            SkinManagerImpl,
            ModelManagerImpl,
            PlayerManagerImpl,
            EntityManager,
            ScriptManagerImpl
        )
    }

    var reloadStartTask: (PackZipper) -> Unit = { callEvent { PluginStartReloadEvent(it) } }
    var reloadEndTask: (ReloadResult) -> Unit = { callEvent { PluginEndReloadEvent(it) } }

    init {
        config = NaturalModelsConfigImpl(PluginConfiguration.CONFIG.create())
    }
}


