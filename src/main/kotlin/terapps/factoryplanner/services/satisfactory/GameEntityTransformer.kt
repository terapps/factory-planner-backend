package terapps.factoryplanner.services.satisfactory

import FGBuildableManufacturer
import FGBuildableManufacturerVariablePower
import FGBuildableResourceExtractor
import FGBuildableWaterPump
import FGBuildingDescriptor
import FGItemDescriptor
import FGItemDescriptorBiomass
import FGItemDescriptorNuclearFuel
import FGResourceDescriptor
import GameEntity
import terapps.factoryplanner.entities.satisfactory.CraftingMachine
import terapps.factoryplanner.entities.satisfactory.Extractor
import terapps.factoryplanner.entities.satisfactory.ItemDescriptor
import terapps.factoryplanner.entities.satisfactory.ItemDescriptorRepository
import kotlin.jvm.optionals.getOrNull

fun String.extractListEntry() = replace("(", "").replace(")", "").split(",")
fun String.extractDictEntry(): MutableList<Map<String, String>> {
    val result = mutableListOf<Map<String, String>>()

    if (isEmpty()) {
        return result
    }
    val outerPairs = this.substring(1, this.length - 1).split("),(")

    for (outerPair in outerPairs) {
        val map = mutableMapOf<String, String>()
        val innerPairs = outerPair.split(",")

        for (innerPair in innerPairs) {
            val (key, value) = innerPair.replace("(", "").replace(")", "").split("=")
            map[key] = value
        }

        result.add(map)
    }

    return result
}

object GameEntityTransformer {
    fun FGItemDescriptor.toItemDescriptor() = ItemDescriptor(
            id = ClassName!!,
            displayName = mDisplayName!!,
            description = mDescription!!,
            form = mForm!!
    )

    fun FGItemDescriptorBiomass.toItemDescriptor() = ItemDescriptor(
            id = ClassName!!,
            displayName = mDisplayName!!,
            description = mDescription!!,
            form = mForm!!
    )
    fun FGItemDescriptorNuclearFuel.toItemDescriptor() = ItemDescriptor(
            id = ClassName!!,
            displayName = mDisplayName!!,
            description = mDescription!!,
            form = mForm!!
    )
    fun FGBuildableManufacturer.toCraftingMachine() = CraftingMachine(
            id = ClassName!!,
            displayName = mDisplayName!!,
            description = mDescription!!,
            manufacturingSpeed = mManufacturingSpeed!!.toFloat(),
            powerConsumption = mPowerConsumption!!.toFloat(),
            powerConsumptionExponent = mPowerConsumptionExponent!!.toFloat(),
            minPotential = mMinPotential!!.toFloat(),
            maxPotential = mMaxPotential!!.toFloat(),
            maxPotentialIncreasePerCrystal = mMaxPotentialIncreasePerCrystal!!.toFloat()
    )

    fun FGBuildableManufacturerVariablePower.toCraftingMachine() = CraftingMachine(
            id = ClassName!!,
            displayName = mDisplayName!!,
            description = mDescription!!,
            manufacturingSpeed = mManufacturingSpeed!!.toFloat(),
            powerConsumption = mPowerConsumption!!.toFloat(),
            powerConsumptionExponent = mPowerConsumptionExponent!!.toFloat(),
            minPotential = mMinPotential!!.toFloat(),
            maxPotential = mMaxPotential!!.toFloat(),
            maxPotentialIncreasePerCrystal = mMaxPotentialIncreasePerCrystal!!.toFloat()
    )

    fun FGBuildableResourceExtractor.toExtractor(itemDescriptorRepository: ItemDescriptorRepository) = Extractor(
            id = ClassName!!,
            displayName = mDisplayName!!,
            description = mDescription!!,
            extractCycleTime = mExtractCycleTime!!.toFloat(),
            itemsPerCycle = mItemsPerCycle!!.toFloat(),
            powerConsumption = mPowerConsumption!!.toFloat(),
            powerConsumptionExponent = mPowerConsumptionExponent!!.toFloat(),
            minPotential = mMinPotential!!.toFloat(),
            maxPotential = mMaxPotential!!.toFloat(),
            maxPotentialIncreasePerCrystal = mMaxPotentialIncreasePerCrystal!!.toFloat(),
            extractorType = mExtractorTypeName!!,
            allowedResources = (mAllowedResources!!.extractListEntry().filterNot { it.isEmpty() }.map {
                val blueprintClassRegex = ".*\\.(.*)\"'".toRegex()
                val descriptor = blueprintClassRegex.matchEntire(it)?.groupValues?.get(1) ?: throw Error("Could not parse $it")

                itemDescriptorRepository.findById(descriptor).getOrNull()!!
            }.takeIf { it.isNotEmpty() } ?: itemDescriptorRepository.findAllByFormInAndIsRawIsTrue(mAllowedResourceForms!!.extractListEntry())).toSet()

    )

    fun FGBuildableWaterPump.toExtractor(itemDescriptorRepository: ItemDescriptorRepository) = Extractor(
            id = ClassName!!,
            displayName = mDisplayName!!,
            description = mDescription!!,
            extractCycleTime = mExtractCycleTime!!.toFloat(),
            itemsPerCycle = mItemsPerCycle!!.toFloat(),
            powerConsumption = mPowerConsumption!!.toFloat(),
            powerConsumptionExponent = mPowerConsumptionExponent!!.toFloat(),
            minPotential = mMinPotential!!.toFloat(),
            maxPotential = mMaxPotential!!.toFloat(),
            maxPotentialIncreasePerCrystal = mMaxPotentialIncreasePerCrystal!!.toFloat(),
            extractorType = mExtractorTypeName!!,
            allowedResources = (mAllowedResources!!.extractListEntry().filterNot { it.isEmpty() }.map {
                val blueprintClassRegex = ".*\\.(.*)\"'".toRegex()
                val descriptor = blueprintClassRegex.matchEntire(it)?.groupValues?.get(1) ?: throw Error("Could not parse $it")

                itemDescriptorRepository.findById(descriptor).getOrNull()!!
            }.takeIf { it.isNotEmpty() } ?: itemDescriptorRepository.findAllByFormInAndIsRawIsTrue(mAllowedResourceForms!!.extractListEntry())).toSet()
    )

    fun FGResourceDescriptor.toItemDescriptor() = ItemDescriptor(
            id = ClassName!!,
            displayName = mDisplayName!!,
            description = mDescription!!,
            form = mForm!!,
            isRaw = true,
    )

    fun FGBuildingDescriptor.toItemDescriptor() = ItemDescriptor(
            id = ClassName!!,
            displayName = mDisplayName!!,
            description = mDescription!!,
            form = mForm!!,
            isBuildable = true
    )

    fun GameEntity.toItemDescriptor() = ItemDescriptor(
            id = ClassName!!,
            displayName = mDisplayName!!,
            description = mDescription!!
    )
}