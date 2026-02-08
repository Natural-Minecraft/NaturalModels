package id.naturalsmp.naturalmodels.bukkit.compatibility.mmoitems

import id.naturalsmp.naturalmodels.bukkit.compatibility.Compatibility
import id.naturalsmp.naturalmodels.bukkit.util.registerListener
import io.lumine.mythic.lib.api.item.NBTItem
import net.Indyuce.mmoitems.MMOItems
import net.Indyuce.mmoitems.api.event.ItemBuildEvent
import net.Indyuce.mmoitems.stat.type.StringStat
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class MMOItemsCompatibility : Compatibility, Listener {

    override fun start() {
        // Register custom stat
        // MMOItems registers stats usually on load, we might be too late if we do it in onEnable.
        // However, we can try.
        MMOItems.plugin.stats.register(NaturalModelStat())
        
        // Register listener for item building to apply NBT
        registerListener(this)
    }

    @EventHandler
    fun onBuild(event: ItemBuildEvent) {
        val item = event.itemStack
        val nbt = NBTItem.get(item)
        if (nbt.hasTag("MMOITEMS_NATURAL_MODEL")) {
            // Logic to apply model if needed directly to item, 
            // but usually NaturalModels checks held item NBT or similar.
            // If NaturalModels scans inventory, we just need the NBT there.
        }
    }
}

class NaturalModelStat : StringStat(
    "NATURAL_MODEL",
    Material.ARMOR_STAND,
    "Natural Model",
    arrayOf("The ID/Name of the NaturalModel", "to apply to this item."),
    arrayOf("natural-model"),
    Material.ARMOR_STAND,
    Material.ARMOR_STAND
) {
    // Standard stat implementation is handled by MMOItems for StringStat
}
