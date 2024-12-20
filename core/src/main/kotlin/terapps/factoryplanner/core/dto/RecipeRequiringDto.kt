package terapps.factoryplanner.core.dto

import terapps.factoryplanner.core.projections.RecipeRequiringSummary
import terapps.factoryplanner.core.services.toDto

class RecipeRequiringDto(
        override val className: String,
        override val manufacturingDuration: Double,
        override val displayName: String,
        override val manufacturedIn: List<CraftingMachineDto>,
        val ingredients: List<ItemIoDto>
) : RecipeDto(className, manufacturingDuration, displayName, manufacturedIn) {
    constructor(recipeSummary: RecipeRequiringSummary) : this(
            recipeSummary.getClassName(),
            recipeSummary.getManufacturingDuration(),
            recipeSummary.getDisplayName(),
            recipeSummary.getManufacturedIn().map {
                CraftingMachineDto(it)
            },
            recipeSummary.getIngredients().map { ItemIoDto(it.getItem().toDto(), it.getOutputPerCycle()) }
    )
}

