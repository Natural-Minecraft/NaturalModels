/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit.nms.v1_21_R3

import id.naturalsmp.naturalmodels.api.armor.PlayerArmor
import id.naturalsmp.naturalmodels.api.nms.Profiled
import id.naturalsmp.naturalmodels.api.player.PlayerSkinParts
import id.naturalsmp.naturalmodels.api.profile.ModelProfile


internal class ProfiledImpl(
    private val playerArmor: PlayerArmor,
    private val modelProfile: () -> ModelProfile,
    private val playerSkinParts: () -> PlayerSkinParts
) : Profiled {

    override fun profile(): ModelProfile = modelProfile()
    override fun armors(): PlayerArmor = playerArmor
    override fun skinParts(): PlayerSkinParts = playerSkinParts()
}


