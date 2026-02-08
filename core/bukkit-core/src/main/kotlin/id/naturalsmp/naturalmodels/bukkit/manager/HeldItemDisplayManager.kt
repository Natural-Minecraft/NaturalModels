package id.naturalsmp.naturalmodels.bukkit.manager

import id.naturalsmp.naturalmodels.api.animation.AnimationModifier
import id.naturalsmp.naturalmodels.api.bone.BoneName
import id.naturalsmp.naturalmodels.api.bukkit.platform.BukkitAdapter
import id.naturalsmp.naturalmodels.api.data.renderer.ModelRenderer
import id.naturalsmp.naturalmodels.api.pack.PackZipper
import id.naturalsmp.naturalmodels.api.tracker.DummyTracker
import id.naturalsmp.naturalmodels.api.tracker.TrackerModifier
import id.naturalsmp.naturalmodels.bukkit.util.registerListener
import id.naturalsmp.naturalmodels.manager.GlobalManager
import id.naturalsmp.naturalmodels.manager.ReloadPipeline
import id.naturalsmp.naturalmodels.util.PLATFORM
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object HeldItemDisplayManager : GlobalManager, Listener {

    private val MODEL_KEY = NamespacedKey("naturalmodels", "model")
    private val mainHandTrackers = ConcurrentHashMap<UUID, DummyTracker>()
    private val offHandTrackers = ConcurrentHashMap<UUID, DummyTracker>()
    
    private val RIGHT_HAND_BONE = BoneName.of("right_hand")
    private val LEFT_HAND_BONE = BoneName.of("left_hand")

    override fun start() {
        registerListener(this)
        PLATFORM.scheduler().asyncTaskTimer(0, 1) {
            syncAll()
        }
    }

    override fun reload(pipeline: ReloadPipeline, zipper: PackZipper) {
        mainHandTrackers.values.forEach { it.close() }
        offHandTrackers.values.forEach { it.close() }
        mainHandTrackers.clear()
        offHandTrackers.clear()
    }

    override fun end() {
        mainHandTrackers.values.forEach { it.close() }
        offHandTrackers.values.forEach { it.close() }
        mainHandTrackers.clear()
        offHandTrackers.clear()
    }

    private fun syncAll() {
        Bukkit.getOnlinePlayers().forEach { player ->
            syncHand(player, EquipmentSlot.HAND, mainHandTrackers)
            syncHand(player, EquipmentSlot.OFF_HAND, offHandTrackers)
        }
    }

    private fun syncHand(player: Player, slot: EquipmentSlot, map: MutableMap<UUID, DummyTracker>) {
        val uuid = player.uniqueId
        val item = if (slot == EquipmentSlot.HAND) player.inventory.itemInMainHand else player.inventory.itemInOffHand
        val modelId = getModelId(item)

        val currentTracker = map[uuid]

        if (modelId == null) {
            currentTracker?.close()
            map.remove(uuid)
            return
        }

        if (currentTracker != null) {
            if (currentTracker.renderer().name() == modelId) {
                // Update position
                updateTrackerPosition(player, slot, currentTracker)
            } else {
                // Model ID changed
                currentTracker.close()
                createNewTracker(player, slot, modelId, map)
            }
        } else {
            // Create new tracker
            createNewTracker(player, slot, modelId, map)
        }
    }

    private fun createNewTracker(player: Player, slot: EquipmentSlot, modelId: String, map: MutableMap<UUID, DummyTracker>) {
        val renderer = PLATFORM.modelManager().renderer(modelId) ?: return
        val tracker = renderer.create(BukkitAdapter.adapt(player.location), TrackerModifier.DEFAULT)
        map[player.uniqueId] = tracker
        tracker.animate("idle", AnimationModifier.DEFAULT)
        updateTrackerPosition(player, slot, tracker)
    }

    private fun updateTrackerPosition(player: Player, slot: EquipmentSlot, tracker: DummyTracker) {
        val registry = id.naturalsmp.naturalmodels.api.tracker.EntityTrackerRegistry.registry(player.uniqueId)
        val modeledPlayer = registry?.trackers()?.firstOrNull { it.renderer().type() == ModelRenderer.Type.PLAYER }

        if (modeledPlayer != null) {
            // Modeled player: follow hand bone
            val boneName = if (slot == EquipmentSlot.HAND) RIGHT_HAND_BONE else LEFT_HAND_BONE
            val bone = modeledPlayer.bone(boneName)
            if (bone != null) {
                // Update location and rotation (from the bone's world position and rotation)
                val pos = bone.worldPosition()
                tracker.location(BukkitAdapter.adapt(Location(player.world, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())))
                // Note: DummyTracker uses its location's yaw/pitch for rotation by default
            } else {
                updateVanillaPosition(player, slot, tracker)
            }
        } else {
            // Vanilla player
            updateVanillaPosition(player, slot, tracker)
        }
    }

    private fun updateVanillaPosition(player: Player, slot: EquipmentSlot, tracker: DummyTracker) {
        val loc = player.eyeLocation
        val direction = loc.direction
        val yaw = loc.yaw
        val pitch = loc.pitch

        // Improved fallback positioning
        val side = if (slot == EquipmentSlot.HAND) 0.35 else -0.35
        val right = direction.clone().crossProduct(org.bukkit.util.Vector(0, 1, 0)).normalize().multiply(side)
        
        loc.add(direction.multiply(0.4)).add(right).add(0.0, -0.4, 0.0)
        
        // Match player's orientation
        loc.yaw = yaw
        loc.pitch = pitch
        
        tracker.location(BukkitAdapter.adapt(loc))
    }

    private fun getModelId(item: ItemStack?): String? {
        if (item == null || item.type == Material.AIR) return null
        val meta = item.itemMeta ?: return null
        
        // 1. Check direct NBT via PDC (NamespacedKey)
        val direct = meta.persistentDataContainer.get(MODEL_KEY, PersistentDataType.STRING)
        if (direct != null) return direct
        
        // 2. MMOItems support (via reflection to avoid dependency issues)
        if (Bukkit.getPluginManager().isPluginEnabled("MMOItems")) {
            try {
                // MythicLib/MMOItems stores stats in a NBT compound called "MMOITEMS_..."
                // or sometimes uses its own NBT wrapper.
                // We'll try to find the "MMOITEMS_NATURAL_MODEL" tag.
                
                // Using reflection to call MythicLib's NBTItem.get(item).getString("MMOITEMS_NATURAL_MODEL")
                val nbtItemClass = Class.forName("io.lumine.mythic.lib.api.item.NBTItem")
                val getMethod = nbtItemClass.getMethod("get", ItemStack::class.java)
                val nbtItem = getMethod.invoke(null, item)
                val getStringMethod = nbtItemClass.getMethod("getString", String::class.java)
                val result = getStringMethod.invoke(nbtItem, "MMOITEMS_NATURAL_MODEL") as? String
                
                if (!result.isNullOrEmpty()) return result
            } catch (ignored: Exception) {
                // If reflection fails, just continue
            }
        }
        
        return null 
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun PlayerItemHeldEvent.held() {
        // Handled by sync task, but can be used for immediate feedback
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun InventoryClickEvent.click() {
        // Handled by sync task
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun PlayerInteractEvent.interact() {
        if (action.name.contains("LEFT_CLICK")) {
            mainHandTrackers[player.uniqueId]?.animate("attack", AnimationModifier.DEFAULT_WITH_PLAY_ONCE)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun EntityDamageByEntityEvent.damage() {
        val damager = damager
        if (damager is Player) {
            mainHandTrackers[damager.uniqueId]?.animate("attack", AnimationModifier.DEFAULT_WITH_PLAY_ONCE)
        }
    }
}
