package terapps.factoryplanner.api.dto

import terapps.factoryplanner.core.entities.CraftingMachine
import java.util.*

open class RecipeDto(
        open val id: UUID,
        open val className: String,
        open val manufacturingDuration: Double,
        open val displayName: String,
        open val manufacturedIn: List<CraftingMachine>
)