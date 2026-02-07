/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.impl.fabric.entity

import id.naturalsmp.naturalmodels.api.fabric.entity.BaseFabricEntity
import id.naturalsmp.naturalmodels.api.fabric.entity.BaseFabricPlayer
import id.naturalsmp.naturalmodels.api.nms.Profiled
import id.naturalsmp.naturalmodels.api.platform.PlatformPlayer
import id.naturalsmp.naturalmodels.api.player.PlayerSkinParts
import id.naturalsmp.naturalmodels.api.profile.ModelProfile
import id.naturalsmp.naturalmodels.impl.fabric.armor.PlayerArmorImpl
import id.naturalsmp.naturalmodels.impl.fabric.seenBy
import id.naturalsmp.naturalmodels.impl.fabric.wrap
import net.minecraft.server.network.ServerPlayerConnection
import java.util.stream.Stream

class BaseFabricPlayerImpl(
    private val connection: ServerPlayerConnection,
    private val profile: () -> ModelProfile,
    private val skinParts: () -> PlayerSkinParts
) : BaseFabricPlayer, BaseFabricEntity by BaseFabricEntityImpl(connection.player), Profiled by ProfiledImpl(PlayerArmorImpl(connection), profile, skinParts) {

    override fun updateInventory() {
        connection.player.containerMenu.sendAllDataToRemote()
    }

    override fun platform(): PlatformPlayer = connection.wrap()

    override fun trackedBy(): Stream<PlatformPlayer> = Stream.concat(
        Stream.of(connection),
        connection.player.seenBy.stream()
    ).map {
        it.wrap()
    }
}


