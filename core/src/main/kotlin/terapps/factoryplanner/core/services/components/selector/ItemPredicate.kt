package terapps.factoryplanner.core.services.components.selector

import terapps.factoryplanner.core.dto.ItemDescriptorDto
import terapps.factoryplanner.core.entities.ItemCategory

fun interface ItemPredicate {
    fun compute(item: ItemDescriptorDto): Boolean
}

val ITEM_IS_LIQUID = ItemPredicate {
    it.form == "RF_LIQUID"
}

val ITEM_IS_GAS = ItemPredicate {
    it.form == "RF_GAS"
}
val JOKER_ITEM = ItemPredicate {
    it.className == "Desc_SAM_C" || it.className == "Desc_SAMIngot_C"
}

val NUCLEAR_WASTE = ItemPredicate {
    it.className == "Desc_NuclearWaste_C"
}
val COAL = ItemPredicate {
    it.className == "Desc_Coal_C"
}
val COMPACTED_COAL = ItemPredicate {
    it.className == "Desc_CompactedCoal_C"
}

val IRON_INGOT = ItemPredicate {
    it.className == "Desc_IronIngot_C"
}

val ITEM_IS_RAW = ItemPredicate {
    it.category == ItemCategory.Raw
}

val WATER = ItemPredicate {
    it.className == "Desc_Water_C"
}