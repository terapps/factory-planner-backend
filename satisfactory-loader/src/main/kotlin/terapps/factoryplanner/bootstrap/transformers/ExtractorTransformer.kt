package terapps.factoryplanner.bootstrap.transformers

import FGBuildableResourceExtractor
import FGBuildableWaterPump
import terapps.factoryplanner.core.entities.Extractor
import terapps.factoryplanner.core.entities.ItemCategory
import terapps.factoryplanner.core.entities.ItemDescriptorRepository

import terapps.factoryplanner.bootstrap.extractListEntry
import kotlin.jvm.optionals.getOrNull

fun FGBuildableResourceExtractor.toExtractor(itemDescriptorRepository: ItemDescriptorRepository) = Extractor(
        id = ClassName,
        displayName = mDisplayName,
        description = mDescription,
        extractCycleTime = mExtractCycleTime.toFloat(),
        itemsPerCycle = mItemsPerCycle.toFloat(),
        powerConsumption = mPowerConsumption.toFloat(),
        powerConsumptionExponent = mPowerConsumptionExponent.toFloat(),
        minPotential = mMinPotential.toFloat(),
        maxPotential = mMaxPotential.toFloat(),
        maxPotentialIncreasePerCrystal = mMaxPotentialIncreasePerCrystal.toFloat(),
        extractorType = mExtractorTypeName,
        allowedResources = (mAllowedResources.extractListEntry().filterNot { it.isEmpty() }.map {
            val blueprintClassRegex = ".*\\.(.*)\"'".toRegex()
            val descriptor = blueprintClassRegex.matchEntire(it)?.groupValues?.get(1) ?: throw Error("Could not parse $it")

            itemDescriptorRepository.findById(descriptor).getOrNull() ?: throw Error("Wouf")
        }.takeIf { it.isNotEmpty() } ?: itemDescriptorRepository.findAllByFormInAndCategory(mAllowedResourceForms.extractListEntry(), ItemCategory.Raw)).toSet()

)

fun FGBuildableWaterPump.toExtractor(itemDescriptorRepository: ItemDescriptorRepository) = Extractor(
        id = ClassName,
        displayName = mDisplayName,
        description = mDescription,
        extractCycleTime = mExtractCycleTime.toFloat(),
        itemsPerCycle = mItemsPerCycle.toFloat(),
        powerConsumption = mPowerConsumption.toFloat(),
        powerConsumptionExponent = mPowerConsumptionExponent.toFloat(),
        minPotential = mMinPotential.toFloat(),
        maxPotential = mMaxPotential.toFloat(),
        maxPotentialIncreasePerCrystal = mMaxPotentialIncreasePerCrystal.toFloat(),
        extractorType = mExtractorTypeName,
        allowedResources = (mAllowedResources.extractListEntry().filterNot { it.isEmpty() }.map {
            val blueprintClassRegex = ".*\\.(.*)\"'".toRegex()
            val descriptor = blueprintClassRegex.matchEntire(it)?.groupValues?.get(1) ?: throw Error("Could not parse $it")

            itemDescriptorRepository.findById(descriptor).getOrNull() ?: throw Error("Wouf")
        }.takeIf { it.isNotEmpty() } ?: itemDescriptorRepository.findAllByFormInAndCategory(mAllowedResourceForms.extractListEntry(), ItemCategory.Raw)).toSet()
)