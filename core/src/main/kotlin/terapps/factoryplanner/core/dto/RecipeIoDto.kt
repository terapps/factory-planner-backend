package terapps.factoryplanner.core.dto

import terapps.factoryplanner.core.projections.RecipeIoSummary

data class RecipeIoDto(
        val item: ItemDescriptorDto,
        val outputPerCycle: Double,
) {
    val actualOutputPerCycle: Double
        get() = if (item.form == "RF_LIQUID") outputPerCycle / 1000f else outputPerCycle

    constructor(summary: RecipeIoSummary): this(
            ItemDescriptorDto(summary.getItem()),
            summary.getOutputPerCycle()
    )
}

