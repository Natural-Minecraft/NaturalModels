/**
 * This source file is part of BetterModel.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit.compatibility.citizens

import id.naturalsmp.naturalmodels.bukkit.compatibility.Compatibility
import id.naturalsmp.naturalmodels.bukkit.compatibility.citizens.command.AnimateCommand
import id.naturalsmp.naturalmodels.bukkit.compatibility.citizens.command.LimbCommand
import id.naturalsmp.naturalmodels.bukkit.compatibility.citizens.command.ModelCommand
import id.naturalsmp.naturalmodels.bukkit.compatibility.citizens.trait.ModelTrait
import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.trait.TraitInfo

class CitizensCompatibility : Compatibility {
    override fun start() {
        CitizensAPI.getTraitFactory()
            .registerTrait(TraitInfo.create(ModelTrait::class.java))
        CitizensAPI.getCommandManager().run {
            register(ModelCommand::class.java)
            register(AnimateCommand::class.java)
            register(LimbCommand::class.java)
        }
    }
}

