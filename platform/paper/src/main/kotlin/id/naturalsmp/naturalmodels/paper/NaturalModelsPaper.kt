/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.paper

import id.naturalsmp.naturalmodels.api.NaturalModelsPlatform
import id.naturalsmp.naturalmodels.bukkit.NaturalModelsPlugin

@Suppress("UNUSED")
class NaturalModelsPaper : NaturalModelsPlugin() {

    override fun jarType(): NaturalModelsPlatform.JarType {
        return NaturalModelsPlatform.JarType.PAPER
    }
}


