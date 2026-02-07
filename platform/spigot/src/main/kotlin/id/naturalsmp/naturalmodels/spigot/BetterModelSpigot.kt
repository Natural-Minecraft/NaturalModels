/**
 * This source file is part of BetterModel.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.spigot

import id.naturalsmp.naturalmodels.api.BetterModelPlatform
import id.naturalsmp.naturalmodels.bukkit.BetterModelPlugin
import id.naturalsmp.naturalmodels.util.toComponent
import id.naturalsmp.naturalmodels.util.warn
import org.bukkit.Bukkit

@Suppress("UNUSED")
class BetterModelSpigot : BetterModelPlugin() {

    override fun onEnable() {
        if (IS_PAPER) {
            warn(
                "You're using Paper, so you have to use Paper jar!".toComponent(),
                "Please download Paper jar from Modrinth! (https://modrinth.com/plugin/bettermodel)".toComponent()
            )
            return Bukkit.getPluginManager().disablePlugin(this)
        }
        super.onEnable()
    }

    override fun jarType(): BetterModelPlatform.JarType {
        return BetterModelPlatform.JarType.SPIGOT
    }
}

