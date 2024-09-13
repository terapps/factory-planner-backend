package terapps.factoryplanner.core.projections

import terapps.factoryplanner.core.entities.CraftingMachine
import java.util.*

interface RecipeSummary {
    fun getId(): UUID
    fun getClassName(): String
    fun getManufacturingDuration(): Float
    fun getDisplayName(): String

    fun getManufacturedIn(): List<CraftingMachine>
}