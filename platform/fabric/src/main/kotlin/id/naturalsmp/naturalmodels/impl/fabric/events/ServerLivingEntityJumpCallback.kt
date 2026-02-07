/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.impl.fabric.events

import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.world.entity.LivingEntity

fun interface ServerLivingEntityJumpCallback {
    fun onJump(entity: LivingEntity)

    companion object {
        @JvmField
        val EVENT = EventFactory.createArrayBacked(ServerLivingEntityJumpCallback::class.java) { callbacks ->
            { entity ->
                callbacks.forEach { it.onJump(entity) }
            }
        }
    }
}


