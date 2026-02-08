/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit.manager

import id.naturalsmp.naturalmodels.api.bukkit.NaturalModelsBukkit
import id.naturalsmp.naturalmodels.api.pack.PackZipper
import id.naturalsmp.naturalmodels.bukkit.compatibility.citizens.CitizensCompatibility
import id.naturalsmp.naturalmodels.bukkit.compatibility.mythicmobs.MythicMobsCompatibility
import id.naturalsmp.naturalmodels.bukkit.compatibility.nexo.NexoCompatibility
import id.naturalsmp.naturalmodels.bukkit.compatibility.skinsrestorer.SkinsRestorerCompatibility
import id.naturalsmp.naturalmodels.bukkit.purpur.PurpurHook
import id.naturalsmp.naturalmodels.bukkit.util.registerListener
import id.naturalsmp.naturalmodels.manager.GlobalManager
import id.naturalsmp.naturalmodels.manager.ReloadPipeline
import id.naturalsmp.naturalmodels.util.info
import id.naturalsmp.naturalmodels.util.toComponent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginEnableEvent

object CompatibilityManager : GlobalManager {

    private val compatibilities = mutableMapOf(
        "MythicMobs" to {
            MythicMobsCompatibility()
        },
        "Citizens" to {
            CitizensCompatibility()
        },
        "SkinsRestorer" to {
            SkinsRestorerCompatibility()
        },
        "Nexo" to {
            NexoCompatibility()
        }
    )

    override fun start() {
        if (NaturalModelsBukkit.IS_PURPUR) PurpurHook.start()
        Bukkit.getPluginManager().run {
            compatibilities.entries.removeIf { (k, v) ->
                if (isPluginEnabled(k)) {
                    try {
                        v().start()
                        k.hookMessage()
                    } catch (e: Exception) {
                        Bukkit.getLogger().warning("[NaturalModels] Failed to hook $k: ${e.message}")
                    }
                    true
                } else false
            }
        }
        registerListener(object : Listener {
            @EventHandler
            fun PluginEnableEvent.enable() {
                val name = plugin.name
                compatibilities.remove(name)?.let {
                    try {
                        it().start()
                        name.hookMessage()
                    } catch (e: Exception) {
                        Bukkit.getLogger().warning("[NaturalModels] Failed to hook $name: ${e.message}")
                    }
                }
            }
        })
    }

    private fun String.hookMessage() = info("Plugin hooks $this".toComponent(NamedTextColor.AQUA))

    override fun reload(pipeline: ReloadPipeline, zipper: PackZipper) {
    }
}


