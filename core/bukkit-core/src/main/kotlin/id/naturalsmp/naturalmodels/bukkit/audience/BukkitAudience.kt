/**
 * This source file is part of BetterModel.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit.audience

import net.kyori.adventure.audience.Audience
import org.bukkit.command.CommandSender

interface BukkitAudience : Audience {
    val sender: CommandSender
}

