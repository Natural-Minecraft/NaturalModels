/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit.audience

import id.naturalsmp.naturalmodels.bukkit.util.audience
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender

class AudienceSender(
    override val sender: CommandSender
) : BukkitAudience {

    private val audience = sender.audience()

    override fun sendMessage(message: Component) {
        audience.sendMessage(message)
    }

    override fun showBossBar(bar: BossBar) {
        audience.showBossBar(bar)
    }

    override fun hideBossBar(bar: BossBar) {
        audience.hideBossBar(bar)
    }
}


