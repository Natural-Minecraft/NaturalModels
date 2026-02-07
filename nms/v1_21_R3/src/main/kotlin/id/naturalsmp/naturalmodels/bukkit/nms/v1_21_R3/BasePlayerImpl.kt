/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit.nms.v1_21_R3

import id.naturalsmp.naturalmodels.api.bukkit.entity.BaseBukkitEntity
import id.naturalsmp.naturalmodels.api.bukkit.entity.BaseBukkitPlayer
import id.naturalsmp.naturalmodels.api.nms.Profiled
import id.naturalsmp.naturalmodels.api.platform.PlatformPlayer
import id.naturalsmp.naturalmodels.api.player.PlayerSkinParts
import id.naturalsmp.naturalmodels.api.profile.ModelProfile
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player
import java.util.stream.Stream

internal data class BasePlayerImpl(
    private val delegate: CraftPlayer,
    private val profile: () -> ModelProfile,
    private val skinParts: () -> PlayerSkinParts
) : BaseBukkitEntity by BaseEntityImpl(delegate), BaseBukkitPlayer, Profiled by ProfiledImpl(PlayerArmorImpl(delegate), profile, skinParts) {

    override fun entity(): Player = delegate

    override fun updateInventory() {
        delegate.handle.containerMenu.sendAllDataToRemote()
    }

    override fun platform(): PlatformPlayer = delegate.wrap()

    override fun trackedBy(): Stream<PlatformPlayer> = Stream.concat(
        Stream.of(delegate),
        delegate.trackedBy.stream()
    ).map {
        it.wrap()
    }
}


