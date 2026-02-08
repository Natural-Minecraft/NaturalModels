package id.naturalsmp.naturalmodels.bukkit.manager

import id.naturalsmp.naturalmodels.api.animation.AnimationModifier
import id.naturalsmp.naturalmodels.api.bukkit.platform.BukkitAdapter
import id.naturalsmp.naturalmodels.api.bukkit.scheduler.BukkitModelScheduler
import id.naturalsmp.naturalmodels.api.data.renderer.RendererGroup
import id.naturalsmp.naturalmodels.api.pack.PackZipper
import id.naturalsmp.naturalmodels.api.tracker.DummyTracker
import id.naturalsmp.naturalmodels.api.tracker.TrackerModifier
import id.naturalsmp.naturalmodels.api.util.function.BonePredicate
import id.naturalsmp.naturalmodels.bukkit.util.registerListener
import id.naturalsmp.naturalmodels.manager.GlobalManager
import id.naturalsmp.naturalmodels.manager.ReloadPipeline
import id.naturalsmp.naturalmodels.util.PLATFORM
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object DamageIndicatorManager : GlobalManager, Listener {

    private const val MODEL_NAME = "damage_indicator"
    private val miniMessage = MiniMessage.miniMessage()
    private val activeIndicators = ConcurrentHashMap<UUID, IndicatorTask>()

    override fun start() {
        registerListener(this)
        PLATFORM.scheduler().asyncTaskTimer(0, 1) {
            update()
        }
    }

    override fun reload(pipeline: ReloadPipeline, zipper: PackZipper) {
        activeIndicators.values.forEach { it.tracker.close() }
        activeIndicators.clear()
    }

    override fun end() {
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
            ?: PLATFORM.modelManager().renderer("steve") // Fallback to steve if indicator model missing
            ?: return

        (PLATFORM.scheduler() as BukkitModelScheduler).task(entity.location) {
            val tracker = renderer.create(BukkitAdapter.adapt(location), TrackerModifier.DEFAULT)
            
            // Create nametag on the root bone (usually the first one or named "root")
            val rootBone = tracker.renderer().rendererGroups().values.firstOrNull()?.name()
            if (rootBone != null) {
                tracker.createNametag(BonePredicate.name(rootBone)) { _, nametag ->
                    nametag.component(component)
                    nametag.alwaysVisible(true)
                }
            }

            val uuid = UUID.randomUUID()
            activeIndicators[uuid] = IndicatorTask(tracker, config.duration)
        }
    }

    private fun update() {
        val iterator = activeIndicators.entries.iterator()
        while (iterator.hasNext()) {
            val (uuid, indicatorTask) = iterator.next()
            if (indicatorTask.ticks <= 0) {
                indicatorTask.tracker.close()
                iterator.remove()
                continue
            }

            // Upward movement
            val loc = indicatorTask.tracker.location()
            indicatorTask.tracker.location(loc.add(0.0, 0.05, 0.0))
            indicatorTask.ticks--
        }
    }

    private data class IndicatorTask(val tracker: DummyTracker, var ticks: Int)
}
```
