/**
 * This source file is part of BetterModel.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.paper

import id.naturalsmp.naturalmodels.api.BetterModelPlatform
import id.naturalsmp.naturalmodels.bukkit.BetterModelPlugin

@Suppress("UNUSED")
class BetterModelPaper : BetterModelPlugin() {

    override fun jarType(): BetterModelPlatform.JarType {
        return BetterModelPlatform.JarType.PAPER
    }
}

