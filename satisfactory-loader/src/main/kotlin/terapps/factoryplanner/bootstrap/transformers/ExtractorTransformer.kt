package terapps.factoryplanner.bootstrap.transformers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.Parameter
import terapps.factoryplanner.bootstrap.dto.generated.FGBuildableFrackingExtractor
import terapps.factoryplanner.bootstrap.dto.generated.FGBuildableResourceExtractor
import terapps.factoryplanner.bootstrap.dto.generated.FGBuildableWaterPump
import terapps.factoryplanner.core.entities.*
import kotlin.jvm.optionals.getOrElse
import kotlin.reflect.KParameter

@Component
class ExtractorTransformer : GenericAbstractTransformer<Any, Extractor>(
        Extractor::class,
        arrayListOf(
                FGBuildableResourceExtractor::class,
                FGBuildableWaterPump::class,
                FGBuildableFrackingExtractor::class,
        )
) {


    @Autowired
    private lateinit var extractorRepository: ExtractorRepository

    override fun save(output: Extractor): Extractor {
        return extractorRepository.save(output)
    }

    override fun Map<*, *>.makeConstructorParams(orig: Any): Map<KParameter, Any?> {
        val truc = mapOf(
                Parameter<Extractor>("className") to this["ClassName"],
                Parameter<Extractor>("displayName") to this["mDisplayName"],
                Parameter<Extractor>("description") to this["mDescription"],
                Parameter<Extractor>("extractCycleTime") to this["mExtractCycleTime"],
                Parameter<Extractor>("itemsPerCycle") to this["mItemsPerCycle"],
                Parameter<Extractor>("powerConsumption") to this["mPowerConsumption"],
                Parameter<Extractor>("powerConsumptionExponent") to this["mPowerConsumptionExponent"],
                Parameter<Extractor>("minPotential") to this["mMinPotential"],
                Parameter<Extractor>("maxPotential") to this["mMaxPotential"],
                Parameter<Extractor>("productionBoost") to this["mBaseProductionBoost"],
                Parameter<Extractor>("extractorType") to this["mExtractorTypeName"],
                Parameter<Extractor>("allowedResourceForm") to (this["mAllowedResourceForms"] as String).extractListEntry().toSet(),
                Parameter<Extractor>("allowedResources") to transformAllowedResources(
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