/**
 * This source file is part of BetterModel.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit.compatibility.skinsrestorer

import id.naturalsmp.naturalmodels.api.profile.ModelProfile
import id.naturalsmp.naturalmodels.api.profile.ModelProfileInfo
import id.naturalsmp.naturalmodels.bukkit.compatibility.Compatibility
import id.naturalsmp.naturalmodels.bukkit.util.wrap
import id.naturalsmp.naturalmodels.manager.ProfileManagerImpl
import id.naturalsmp.naturalmodels.manager.SkinManagerImpl
import id.naturalsmp.naturalmodels.util.PLATFORM
import net.skinsrestorer.api.SkinsRestorerProvider
import net.skinsrestorer.api.event.SkinApplyEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

class SkinsRestorerCompatibility : Compatibility {

    private val manager by lazy {
        SkinsRestorerProvider.get()
    }

    override fun start() {
        manager.eventBus.subscribe(PLATFORM, SkinApplyEvent::class.java) {
            val player = it.getPlayer(Player::class.java)
            SkinManagerImpl.removeCache(ModelProfile.of(player.wrap()))
        }
        ProfileManagerImpl.supplier {
            SkinsRestorerProfile(it)
        }
    }

    private inner class SkinsRestorerProfile(
        private val info: ModelProfileInfo
    ) : ModelProfile.Uncompleted {
        override fun info(): ModelProfileInfo = info

        override fun complete(): CompletableFuture<ModelProfile> = CompletableFuture.supplyAsync {
            manager.playerStorage
                .getSkinForPlayer(
                    info.id,
                    info.name,
                    Bukkit.getOnlineMode()
                ).map { skin ->
                    ModelProfile.of(
                        info,
                        ProfileManagerImpl.skin(skin.value)
                    )
                }.orElse(null)
        }

    }
}

