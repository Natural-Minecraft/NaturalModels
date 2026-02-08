package id.naturalsmp.naturalmodels.bukkit.compatibility.mmoitems

import id.naturalsmp.naturalmodels.bukkit.compatibility.Compatibility
import net.Indyuce.mmoitems.MMOItems
import net.Indyuce.mmoitems.stat.type.StringStat
import org.bukkit.Material

class MMOItemsCompatibility : Compatibility {

    override fun start() {
        // Register custom stat for NaturalModels
        MMOItems.plugin.stats.register(NaturalModelStat())
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
    // NBT tag "MMOITEMS_NATURAL_MODEL" is automatically applied by MMOItems
}
