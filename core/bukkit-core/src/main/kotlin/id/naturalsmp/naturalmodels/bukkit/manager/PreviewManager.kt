package id.naturalsmp.naturalmodels.bukkit.manager

import id.naturalsmp.naturalmodels.api.animation.AnimationIterator
import id.naturalsmp.naturalmodels.api.animation.AnimationModifier
import id.naturalsmp.naturalmodels.api.bukkit.platform.BukkitAdapter
import id.naturalsmp.naturalmodels.api.pack.PackZipper
import id.naturalsmp.naturalmodels.api.tracker.DummyTracker
import id.naturalsmp.naturalmodels.api.tracker.TrackerModifier
import id.naturalsmp.naturalmodels.bukkit.util.registerListener
import id.naturalsmp.naturalmodels.manager.GlobalManager
import id.naturalsmp.naturalmodels.manager.ReloadPipeline
import id.naturalsmp.naturalmodels.util.PLATFORM
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.util.Vector
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object PreviewManager : GlobalManager, Listener {

    private val activePreviews = ConcurrentHashMap<UUID, DummyTracker>()

    override fun start() {
        registerListener(this)
    }

    override fun reload(pipeline: ReloadPipeline, zipper: PackZipper) {
        clearAll()
    }

    override fun end() {
        clearAll()
    }

    private fun clearAll() {
        activePreviews.values.forEach { it.close() }
        activePreviews.clear()
    }

    fun startPreview(player: Player, modelId: String, animation: String? = null, scale: Float = 1.0f) {
        val uuid = player.uniqueId
        
        // Remove existing preview if any
        activePreviews.remove(uuid)?.close()

        val renderer = PLATFORM.modelManager().renderer(modelId) ?: run {
            player.sendMessage("§cUnable to find model: $modelId")
            return
        }

        // Calculate position (3 blocks in front of player)
        val loc = player.eyeLocation.clone()
        val dir = loc.direction.clone().setY(0).normalize()
        loc.add(dir.multiply(3.0)).subtract(0.0, 1.0, 0.0)
        loc.yaw += 180f // Face the player

        val tracker = renderer.create(BukkitAdapter.adapt(loc), TrackerModifier.DEFAULT)
        activePreviews[uuid] = tracker

        // Set scale
        tracker.pipeline.scale { scale }

        // Play animation if provided
        if (animation != null) {
            val modifier = AnimationModifier(0, 0, AnimationIterator.Type.LOOP)
            if (!tracker.animate(animation, modifier)) {
                player.sendMessage("§eModel spawned, but animation '$animation' not found.")
            }
        } else {
            // Default to idle if exists
            tracker.animate("idle", AnimationModifier.DEFAULT)
        }

        // Spawn for player
        tracker.spawn(BukkitAdapter.adapt(player))
        
        player.sendMessage("§aStarted preview of $modelId.")
    }

    fun clearPreview(player: Player): Boolean {
        val tracker = activePreviews.remove(player.uniqueId)
        tracker?.close()
        return tracker != null
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onQuit(event: PlayerQuitEvent) {
        activePreviews.remove(event.player.uniqueId)?.close()
    }
}
