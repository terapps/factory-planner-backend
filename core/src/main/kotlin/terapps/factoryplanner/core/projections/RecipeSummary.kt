package terapps.factoryplanner.core.projections

import terapps.factoryplanner.core.entities.CraftingMachine
import java.util.*

interface RecipeSummary {
    fun getId(): UUID
    fun getClassName(): String
    fun getManufacturingDuration(): Double
    fun getDisplayName(): String

    fun getManufacturedIn(): List<CraftingMachine>
}