/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.impl.fabric.profile

import com.mojang.authlib.GameProfile
import id.naturalsmp.naturalmodels.api.profile.ModelProfile
import id.naturalsmp.naturalmodels.api.profile.ModelProfileInfo
import id.naturalsmp.naturalmodels.api.profile.ModelProfileSkin
import id.naturalsmp.naturalmodels.util.PLATFORM

class ModelProfileImpl(private val profile: GameProfile) : ModelProfile {
    private val info = ModelProfileInfo(
        profile.id,
        profile.name
    )

    private val skin by lazy {
        val properties = profile.properties
        val property = properties["textures"].firstOrNull()

        if (property == null) {
            ModelProfileSkin.EMPTY
        } else {
            PLATFORM.profileManager().skin(property.value)
        }
    }

    override fun info(): ModelProfileInfo = info

    override fun skin(): ModelProfileSkin = skin
}


