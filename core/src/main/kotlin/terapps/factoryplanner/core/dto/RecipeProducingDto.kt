package terapps.factoryplanner.core.dto

import terapps.factoryplanner.core.projections.RecipeProducingSummary
import java.util.*

data class RecipeProducingDto(
        override val className: String,
        override val manufacturingDuration: Double,
        override val displayName: String,
        override val manufacturedIn: List<CraftingMachineDto>,
        val producing: List<RecipeIoDto>
) : RecipeDto(className, manufacturingDuration, displayName, manufacturedIn) {
    val manufacturingDurationByMinute: Double
        get() = 60.0 / manufacturingDuration

    constructor(recipeSummary: RecipeProducingSummary) : this(
            recipeSummary.getClassName(),
            recipeSummary.getManufacturingDuration(),
            recipeSummary.getDisplayName(),
            recipeSummary.getManufacturedIn().map {
                CraftingMachineDto(it)
            },
            recipeSummary.getProducing().map { RecipeIoDto(it) }
    )
}

