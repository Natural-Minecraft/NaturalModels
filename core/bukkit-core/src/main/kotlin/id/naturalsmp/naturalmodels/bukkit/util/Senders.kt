/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit.util

import id.naturalsmp.naturalmodels.bukkit.NaturalModelsLibrary
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.command.CommandSender

val ADVENTURE_PLATFORM = if (NaturalModelsLibrary.ADVENTURE_PLATFORM.isLoaded) BukkitAudiences.create(PLUGIN) else null

fun CommandSender.audience() = ADVENTURE_PLATFORM?.sender(this) ?: this


