/**
 * This source file is part of BetterModel.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit.purpur

import id.naturalsmp.naturalmodels.api.BetterModel
import id.naturalsmp.naturalmodels.api.bukkit.platform.BukkitPlayer
import id.naturalsmp.naturalmodels.api.event.CreateDummyTrackerEvent
import id.naturalsmp.naturalmodels.api.event.CreateEntityTrackerEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

object PurpurHook {
    fun start() {
        val platform = BetterModel.platform()
        val config = BetterModel.config()
        platform.logger().info(
            Component.text("BetterModel is currently running in Purpur.").color(NamedTextColor.LIGHT_PURPLE),
            Component.text("Some Purpur features will be enabled.").color(NamedTextColor.LIGHT_PURPLE)
        )
        BetterModel.eventBus().subscribe(platform, CreateDummyTrackerEvent::class.java) { event ->
            event.tracker().pipeline.viewFilter {
                !config.usePurpurAfk() || !(it as BukkitPlayer).source().isAfk
            }
        }
        BetterModel.eventBus().subscribe(platform, CreateEntityTrackerEvent::class.java) { event ->
            event.tracker().pipeline.viewFilter {
                !config.usePurpurAfk() || !(it as BukkitPlayer).source().isAfk
            }
        }
    }
}

