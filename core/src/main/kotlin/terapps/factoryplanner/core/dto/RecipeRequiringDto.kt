package terapps.factoryplanner.core.dto

import terapps.factoryplanner.core.entities.CraftingMachine
import java.util.*

data class RecipeRequiringDto(
        override val id: UUID,
        override val className: String,
        override val manufacturingDuration: Double,
        override val displayName: String,
        override val manufacturedIn: List<CraftingMachine>,
        val ingredients: List<RecipeIoDto>
) : RecipeDto(id, className, manufacturingDuration, displayName, manufacturedIn)

