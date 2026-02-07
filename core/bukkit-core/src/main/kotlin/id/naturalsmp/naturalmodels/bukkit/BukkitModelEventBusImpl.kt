/**
 * This source file is part of BetterModel.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit

import id.naturalsmp.naturalmodels.BetterModelEventBusImpl
import id.naturalsmp.naturalmodels.api.BetterModelEventBus
import id.naturalsmp.naturalmodels.api.bukkit.BukkitModelEventBus
import id.naturalsmp.naturalmodels.api.bukkit.event.BetterModelBukkitEvent
import id.naturalsmp.naturalmodels.api.event.CancellableEvent
import org.bukkit.Bukkit

class BukkitModelEventBusImpl : BukkitModelEventBus, BetterModelEventBus by BetterModelEventBusImpl({ eventClass, supplier ->
    BetterModelBukkitEvent(eventClass, supplier).apply {
        Bukkit.getPluginManager().callEvent(this)
    }.source()?.let { event ->
        if (event !is CancellableEvent || !event.isCancelled()) BetterModelEventBus.Result.SUCCESS else BetterModelEventBus.Result.FAIL
    } ?: BetterModelEventBus.Result.NO_EVENT_HANDLER
})

