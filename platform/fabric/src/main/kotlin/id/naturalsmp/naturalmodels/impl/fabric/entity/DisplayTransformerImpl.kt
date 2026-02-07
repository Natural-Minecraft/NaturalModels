/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.impl.fabric.entity

import id.naturalsmp.naturalmodels.api.nms.DisplayTransformer
import id.naturalsmp.naturalmodels.api.nms.PacketBundler
import id.naturalsmp.naturalmodels.api.util.lock.SingleLock
import id.naturalsmp.naturalmodels.impl.fabric.network.plusAssign
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
import net.minecraft.world.entity.Display
import org.joml.Quaternionf
import org.joml.Vector3f

class DisplayTransformerImpl(source: Display.ItemDisplay) :
    DisplayTransformer
{
    private val id = source.id

    private val entityData = TransformationData()
    private val entityDataLock = SingleLock()

    override fun transform(
        duration: Int,
        position: Vector3f,
        scale: Vector3f,
        rotation: Quaternionf,
        bundler: PacketBundler
    ) {
        entityDataLock.accessToLock {
            entityData.transform(
                duration,
                position,
                scale,
                rotation
            )
            entityData.packDirty()
        }?.let {
            bundler += ClientboundSetEntityDataPacket(id, it)
        }
    }

    override fun sendTransformation(bundler: PacketBundler) {
        entityDataLock.accessToLock {
            entityData.pack()
        }?.let {
            bundler += ClientboundSetEntityDataPacket(id, it)
        }
    }
}


