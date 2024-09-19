package terapps.factoryplanner.core.dto

data class RecipeIoDto(
        val item: ItemDescriptorDto,
        val outputPerCycle: Double,
) {
    val actualOutputPerCycle: Double
        get() = if (item.form == "RF_LIQUID") outputPerCycle / 1000f else outputPerCycle
}

