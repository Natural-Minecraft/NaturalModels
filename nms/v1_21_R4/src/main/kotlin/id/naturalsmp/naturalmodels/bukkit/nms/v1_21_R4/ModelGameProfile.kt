/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit.nms.v1_21_R4

import com.mojang.authlib.GameProfile
import id.naturalsmp.naturalmodels.api.NaturalModels
import id.naturalsmp.naturalmodels.api.profile.ModelProfile
import id.naturalsmp.naturalmodels.api.profile.ModelProfileInfo
import id.naturalsmp.naturalmodels.api.profile.ModelProfileSkin

internal data class ModelGameProfile(
    private val gameProfile: GameProfile
) : ModelProfile {

    private val info = ModelProfileInfo(gameProfile.id, gameProfile.name)
    private val skin by lazy {
        gameProfile.properties["textures"].firstOrNull()?.let {
            NaturalModels.platform().profileManager().skin(it.value)
        } ?: ModelProfileSkin.EMPTY
    }

    override fun info(): ModelProfileInfo = info

    override fun skin(): ModelProfileSkin = skin
}


