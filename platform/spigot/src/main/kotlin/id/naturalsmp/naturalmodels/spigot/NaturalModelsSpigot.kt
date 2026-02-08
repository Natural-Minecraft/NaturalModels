/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 NaturalModels
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.spigot

import id.naturalsmp.naturalmodels.api.NaturalModelsPlatform
import id.naturalsmp.naturalmodels.bukkit.NaturalModelsPlugin
import id.naturalsmp.naturalmodels.util.toComponent
import id.naturalsmp.naturalmodels.util.warn
import org.bukkit.Bukkit

@Suppress("UNUSED")
class NaturalModelsSpigot : NaturalModelsPlugin() {

    override fun onEnable() {
        if (IS_PAPER) {
            warn(
                "You're using Paper, so you have to use Paper jar!".toComponent(),
                "Please download Paper jar from Modrinth! (https://modrinth.com/plugin/NaturalModels)".toComponent()
            )
            return Bukkit.getPluginManager().disablePlugin(this)
        }
        super.onEnable()
    }

    override fun jarType(): NaturalModelsPlatform.JarType {
        return NaturalModelsPlatform.JarType.SPIGOT
    }
}


