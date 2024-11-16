package terapps.factoryplanner.core.dto

import terapps.factoryplanner.core.entities.CraftingMachineEntity
import terapps.factoryplanner.core.projections.RecipeRequiringSummary
import terapps.factoryplanner.core.projections.RecipeSummary
import terapps.factoryplanner.core.services.toDto
import java.util.*

open class RecipeDto(
        open val className: String,
        open val manufacturingDuration: Double,
        open val displayName: String,
        open val manufacturedIn: List<CraftingMachineDto>
) {
    constructor(recipeSummary: RecipeSummary): this(
            recipeSummary.getClassName(),
            recipeSummary.getManufacturingDuration(),
            recipeSummary.getDisplayName(),
            recipeSummary.getManufacturedIn().map {
                CraftingMachineDto(it)
            },
    )
}