/**
 * This source file is part of NaturalModels.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.impl.fabric

import id.naturalsmp.naturalmodels.api.fabric.platform.*
import id.naturalsmp.naturalmodels.api.fabric.platform.FabricAdapter.adapt
import id.naturalsmp.naturalmodels.api.platform.*
import net.minecraft.server.network.ServerPlayerConnection
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

val PlatformLocation.asFabric get() = this as FabricLocation

fun Entity.wrap() = adapt(this)
fun LivingEntity.wrap() = adapt(this)
fun ServerPlayerConnection.wrap() = adapt(this)
fun ItemStack.wrap() = adapt(this)

fun PlatformEntity.unwarp(): Entity = (this as FabricEntity).source()
fun PlatformLivingEntity.unwarp(): LivingEntity = (this as FabricLivingEntity).source()
fun PlatformPlayer.unwarp(): ServerPlayerConnection = (this as FabricPlayer).source()
fun PlatformItemStack.unwarp(): ItemStack = (this as FabricItemStack).source()


