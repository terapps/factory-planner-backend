package terapps.factoryplanner.core.projections

import terapps.factoryplanner.core.entities.CraftingMachineEntity
import java.util.*

interface RecipeSummary {
    fun getClassName(): String
    fun getDisplayName(): String
    fun getManufacturingDuration(): Double
    fun getManufacturedIn(): List<CraftingMachineEntity>
}