package terapps.factoryplanner.bootstrap.dto.generated

import terapps.factoryplanner.bootstrap.dto.GameEntity

data class FGCustomizationRecipe(
        val `ClassName`: String,
        val `FullName`: String,
        val `mDisplayName`: String,
        val `mIngredients`: String,
        val `mManualManufacturingMultiplier`: Double,
        val `mManufactoringDuration`: Double,
        val `mManufacturingMenuPriority`: Double,
        val `mProducedIn`: String,
        val `mProduct`: String,
        val `mRelevantEvents`: String,
        val `mVariablePowerConsumptionConstant`: Double,
        val `mVariablePowerConsumptionFactor`: Double
) : GameEntity()
    