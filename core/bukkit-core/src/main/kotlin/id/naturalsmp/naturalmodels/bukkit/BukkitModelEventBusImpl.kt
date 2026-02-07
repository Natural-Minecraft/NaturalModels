/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit

import id.naturalsmp.naturalmodels.NaturalModelsEventBusImpl
import id.naturalsmp.naturalmodels.api.NaturalModelsEventBus
import id.naturalsmp.naturalmodels.api.bukkit.BukkitModelEventBus
import id.naturalsmp.naturalmodels.api.bukkit.event.NaturalModelsBukkitEvent
import id.naturalsmp.naturalmodels.api.event.CancellableEvent
import org.bukkit.Bukkit

class BukkitModelEventBusImpl : BukkitModelEventBus, NaturalModelsEventBus by NaturalModelsEventBusImpl({ eventClass, supplier ->
    NaturalModelsBukkitEvent(eventClass, supplier).apply {
        Bukkit.getPluginManager().callEvent(this)
    }.source()?.let { event ->
        if (event !is CancellableEvent || !event.isCancelled()) NaturalModelsEventBus.Result.SUCCESS else NaturalModelsEventBus.Result.FAIL
    } ?: NaturalModelsEventBus.Result.NO_EVENT_HANDLER
})


