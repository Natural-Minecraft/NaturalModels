/**
 * This source file is part of BetterModel.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.profile

import id.naturalsmp.naturalmodels.api.platform.PlatformPlayer
import id.naturalsmp.naturalmodels.api.profile.ModelProfile
import id.naturalsmp.naturalmodels.api.profile.ModelProfileInfo
import id.naturalsmp.naturalmodels.api.profile.ModelProfileSupplier
import id.naturalsmp.naturalmodels.util.PLATFORM

class DefaultHttpModelProfileSupplier : ModelProfileSupplier {

    private val http = HttpModelProfileSupplier()

    override fun supply(info: ModelProfileInfo): ModelProfile.Uncompleted {
        val player = PLATFORM.adapter().offlinePlayer(info.id)
        return if (player is PlatformPlayer) ModelProfile.of(player).asUncompleted() else http.supply(info)
    }
}

