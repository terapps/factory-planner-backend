package terapps.factoryplanner.bootstrap.transformers.entities

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.core.services.components.Parameter
import terapps.factoryplanner.bootstrap.dto.generated.FGBuildableFrackingExtractor
import terapps.factoryplanner.bootstrap.dto.generated.FGBuildableResourceExtractor
import terapps.factoryplanner.bootstrap.dto.generated.FGBuildableWaterPump
import terapps.factoryplanner.bootstrap.extractListEntry
import terapps.factoryplanner.bootstrap.toBoolean
import terapps.factoryplanner.bootstrap.transformers.BatchList
import terapps.factoryplanner.bootstrap.transformers.GenericAbstractTransformer
import terapps.factoryplanner.core.entities.*
import terapps.factoryplanner.core.repositories.ExtractorRepository
import kotlin.reflect.KParameter

@Component
class ExtractorTransformer : GenericAbstractTransformer<Any, ExtractorEntity>(
        ExtractorEntity::class,
        arrayListOf(
                FGBuildableResourceExtractor::class,
                FGBuildableWaterPump::class,
                FGBuildableFrackingExtractor::class,
        )
) {

    @Autowired
    private lateinit var extractorRepository: ExtractorRepository

    override val batch: BatchList<ExtractorEntity> = BatchList() {
        extractorRepository.saveAll(it)
    }

    override fun Map<*, *>.makeConstructorParams(orig: Any): Map<KParameter, Any?> {
        val truc = mapOf(
                Parameter<ExtractorEntity>("className") to this["ClassName"],
                Parameter<ExtractorEntity>("displayName") to this["mDisplayName"],
                Parameter<ExtractorEntity>("description") to this["mDescription"],
                Parameter<ExtractorEntity>("extractCycleTime") to this["mExtractCycleTime"],
                Parameter<ExtractorEntity>("itemsPerCycle") to this["mItemsPerCycle"],
                Parameter<ExtractorEntity>("powerConsumption") to this["mPowerConsumption"],
                Parameter<ExtractorEntity>("powerConsumptionExponent") to this["mPowerConsumptionExponent"],
                Parameter<ExtractorEntity>("minPotential") to this["mMinPotential"],
                Parameter<ExtractorEntity>("maxPotential") to this["mMaxPotential"],
                Parameter<ExtractorEntity>("productionBoost") to this["mBaseProductionBoost"],
                Parameter<ExtractorEntity>("extractorType") to this["mExtractorTypeName"],
                Parameter<ExtractorEntity>("matchByAllowedResources") to this["mOnlyAllowCertainResources"]?.toString()?.toBoolean(),
                Parameter<ExtractorEntity>("allowedResourceForm") to (this["mAllowedResourceForms"] as String).extractListEntry().toSet(),
                Parameter<ExtractorEntity>("allowedResources") to transformAllowedResources(
                        this["mAllowedResources"] as String,
                ).toSet()
        )

        return truc
    }

    private fun transformAllowedResources(allowedResources: String): List<String> {
        return allowedResources.extractListEntry().map {
            val blueprintClassRegex = ".*\\.(.*)'".toRegex()

            val descriptor = blueprintClassRegex.matchEntire(it)?.groupValues?.get(1)
                    ?: throw Error("Could not parse $it")

            descriptor
        }
    }


}