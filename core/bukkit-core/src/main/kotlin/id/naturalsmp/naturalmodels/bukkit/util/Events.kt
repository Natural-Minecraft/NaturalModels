/**
 * This source file is part of BetterModel.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit.util

import org.bukkit.Bukkit
import org.bukkit.event.Listener

fun registerListener(listener: Listener) {
    Bukkit.getPluginManager().registerEvents(listener, PLUGIN)
}

