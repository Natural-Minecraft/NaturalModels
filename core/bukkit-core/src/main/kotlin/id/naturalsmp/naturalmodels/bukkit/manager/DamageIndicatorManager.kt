package id.naturalsmp.naturalmodels.bukkit.manager

import id.naturalsmp.naturalmodels.api.NaturalModels
import id.naturalsmp.naturalmodels.api.animation.AnimationModifier
import id.naturalsmp.naturalmodels.api.manager.GlobalManager
import id.naturalsmp.naturalmodels.api.tracker.DummyTracker
import id.naturalsmp.naturalmodels.api.tracker.TrackerModifier
import id.naturalsmp.naturalmodels.manager.ReloadPipeline
import id.naturalsmp.naturalmodels.pack.PackZipper
import id.naturalsmp.naturalmodels.util.PLATFORM
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object DamageIndicatorManager : GlobalManager, Listener {

    private val miniMessage = MiniMessage.miniMessage()
    private val activeIndicators = ConcurrentHashMap<UUID, IndicatorTask>()
    private const val MODEL_NAME = "damage_indicator"

    override fun start() {
        registerListener(this)
        PLATFORM.scheduler().asyncTaskTimer(0, 1) {
            tick()
        }
    }

    override fun reload(pipeline: ReloadPipeline, zipper: PackZipper) {
        clearAll()
    }

    override fun end() {
        clearAll()
    }

    private fun clearAll() {
        activeIndicators.values.forEach { it.tracker.close() }
        activeIndicators.clear()
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onEntityDamage(event: EntityDamageEvent) {
        val config = PLATFORM.config().damageIndicator()
        if (!config.enabled) return

        val entity = event.entity
        if (entity !is LivingEntity) return

        val damage = event.finalDamage
        if (damage <= 0) return

        val location = entity.location.add(0.0, config.offsetY, 0.0)
        val text = config.format.replace("<damage>", String.format("%.1f", damage))
        val component = miniMessage.deserialize(text)

        // We need a model named "damage_indicator" or similar.
        // If not found, we fallback to a simple dummy if possible, or just skip.
        val renderer = PLATFORM.modelManager().renderer(MODEL_NAME) 
            ?: PLATFORM.modelManager().renderer("steve") // Fallback to steve's root if indicator model missing
            ?: return

        PLATFORM.scheduler().runTask {
            val tracker = renderer.create(PLATFORM.adapter().adapt(location), TrackerModifier.DEFAULT)
            
            // Create nametag on the root bone (usually the first one or named "root")
            val rootBone = tracker.renderer().rendererGroups.values.firstOrNull()?.name 
            if (rootBone != null) {
                tracker.createNametag({ it.name() == rootBone }) { nametag ->
                    nametag.component(component)
                    nametag.alwaysVisible(true)
                }
            }

            val uuid = UUID.randomUUID()
            activeIndicators[uuid] = IndicatorTask(tracker, config.duration)
        }
    }

    private fun tick() {
        val iterator = activeIndicators.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            val task = entry.value
            
            if (task.remainingTicks <= 0) {
                task.tracker.close()
                iterator.remove()
                continue
            }

            // Move upwards
            val currentLoc = task.tracker.location()
            val newLoc = currentLoc.add(0.0, 0.05, 0.0)
            task.tracker.location(newLoc)

            task.remainingTicks--
        }
    }

    private class IndicatorTask(val tracker: DummyTracker, var remainingTicks: Int)
}
