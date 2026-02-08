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
        MMOItems.plugin.stats.register(NaturalModelStat())
        
        // Register listener for item building to apply NBT
        registerListener(this)
    }

    @EventHandler
    fun onBuild(event: ItemBuildEvent) {
        val item = event.itemStack
        val nbt = NBTItem.get(item)
        if (nbt.hasTag("MMOITEMS_NATURAL_MODEL")) {
            // NBT is already applied by MMOItems stat system
            // NaturalModels can read this NBT when player holds the item
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
