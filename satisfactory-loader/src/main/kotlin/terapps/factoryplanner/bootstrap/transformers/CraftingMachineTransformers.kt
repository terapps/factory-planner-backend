package terapps.factoryplanner.bootstrap.transformers


import FGBuildableManufacturer
import FGBuildableManufacturerVariablePower
import terapps.factoryplanner.core.entities.CraftingMachine

fun FGBuildableManufacturer.toCraftingMachine() = CraftingMachine(
        id = ClassName,
        displayName = mDisplayName,
        description = mDescription,
        manufacturingSpeed = mManufacturingSpeed.toFloat(),
        powerConsumption = mPowerConsumption.toFloat(),
        powerConsumptionExponent = mPowerConsumptionExponent.toFloat(),
        minPotential = mMinPotential.toFloat(),
        maxPotential = mMaxPotential.toFloat(),
        maxPotentialIncreasePerCrystal = mMaxPotentialIncreasePerCrystal.toFloat()
)

fun FGBuildableManufacturerVariablePower.toCraftingMachine() = CraftingMachine(
        id = ClassName,
        displayName = mDisplayName,
        description = mDescription,
        manufacturingSpeed = mManufacturingSpeed.toFloat(),
        powerConsumption = mPowerConsumption.toFloat(),
        powerConsumptionExponent = mPowerConsumptionExponent.toFloat(),
        minPotential = mMinPotential.toFloat(),
        maxPotential = mMaxPotential.toFloat(),
        maxPotentialIncreasePerCrystal = mMaxPotentialIncreasePerCrystal.toFloat()
)