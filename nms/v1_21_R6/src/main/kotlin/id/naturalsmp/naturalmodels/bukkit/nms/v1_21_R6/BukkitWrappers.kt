/**
 * This source file is part of BetterModel.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit.nms.v1_21_R6

import id.naturalsmp.naturalmodels.api.bukkit.platform.BukkitAdapter.*
import id.naturalsmp.naturalmodels.api.bukkit.platform.*
import id.naturalsmp.naturalmodels.api.bukkit.platform.BukkitItemStack
import id.naturalsmp.naturalmodels.api.platform.*
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun Entity.wrap() = adapt(this)
fun LivingEntity.wrap() = adapt(this)
fun OfflinePlayer.wrap() = adapt(this)
fun Player.wrap() = adapt(this)
fun Location.wrap() = adapt(this)
fun World.wrap() = adapt(this)
fun ItemStack.wrap() = adapt(this)

fun PlatformEntity.unwarp(): Entity = (this as BukkitEntity).source()
fun PlatformLivingEntity.unwarp(): LivingEntity = (this as BukkitLivingEntity).source()
fun PlatformOfflinePlayer.unwarp(): OfflinePlayer = (this as BukkitOfflinePlayer).source()
fun PlatformPlayer.unwarp(): Player = (this as BukkitPlayer).source()
fun PlatformLocation.unwarp(): Location = (this as BukkitLocation).source()
fun PlatformWorld.unwarp(): World = (this as BukkitWorld).source()
fun PlatformItemStack.unwarp(): ItemStack = (this as BukkitItemStack).source()

