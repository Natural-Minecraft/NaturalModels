/**
 * This source file is part of BetterModel.
 * Copyright (c) 2024–2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */
package id.naturalsmp.naturalmodels.bukkit.manager

import id.naturalsmp.naturalmodels.api.manager.PlayerManager
import id.naturalsmp.naturalmodels.api.nms.PlayerChannelHandler
import id.naturalsmp.naturalmodels.api.pack.PackZipper
import id.naturalsmp.naturalmodels.api.platform.PlatformPlayer
import id.naturalsmp.naturalmodels.bukkit.util.registerListener
import id.naturalsmp.naturalmodels.bukkit.util.wrap
import id.naturalsmp.naturalmodels.manager.GlobalManager
import id.naturalsmp.naturalmodels.manager.ReloadPipeline
import id.naturalsmp.naturalmodels.manager.SkinManagerImpl
import id.naturalsmp.naturalmodels.util.PLATFORM
import id.naturalsmp.naturalmodels.util.handleFailure
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object PlayerManagerImpl : PlayerManager, GlobalManager {

    private val playerMap = ConcurrentHashMap<UUID, PlayerChannelHandler>()

    override fun start() {
        registerListener(object : Listener {
            @EventHandler(priority = EventPriority.HIGHEST)
            fun PlayerJoinEvent.join() {
                if (player.isOnline) runCatching { //For fake player
                    player.wrap().register()
                }.handleFailure {
                    "Unable to load ${player.name}'s data."
                }
            }
            @EventHandler(priority = EventPriority.MONITOR)
            fun PlayerQuitEvent.quit() {
                playerMap.remove(player.uniqueId)?.use {
                    SkinManagerImpl.removeCache(it.base().profile())
                }
            }
        })
    }

    private fun PlatformPlayer.register() = playerMap.computeIfAbsent(uuid()) {
        PLATFORM.nms().inject(this)
    }.apply {
        SkinManagerImpl.complete(base().profile().asUncompleted())
    }

    override fun reload(pipeline: ReloadPipeline, zipper: PackZipper) {
    }

    override fun end() {
        playerMap.values.removeIf {
            it.use { used -> SkinManagerImpl.removeCache(used.base().profile()) }
            true
        }
    }


    override fun player(uuid: UUID): PlayerChannelHandler? = playerMap[uuid]
    override fun player(player: PlatformPlayer): PlayerChannelHandler = player.register()
}

