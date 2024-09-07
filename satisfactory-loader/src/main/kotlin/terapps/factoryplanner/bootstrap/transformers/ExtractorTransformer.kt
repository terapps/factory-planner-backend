package terapps.factoryplanner.bootstrap.transformers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.Parameter
import terapps.factoryplanner.bootstrap.dto.generated.FGBuildableResourceExtractor
import terapps.factoryplanner.bootstrap.dto.generated.FGBuildableWaterPump
import terapps.factoryplanner.core.entities.*
import kotlin.jvm.optionals.getOrElse
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

@Component
class ExtractorTransformer : GenericAbstractTransformer<Any, Extractor>(
        Extractor::class,
        arrayListOf(
                FGBuildableResourceExtractor::class,
                FGBuildableWaterPump::class,
        )
) {


    @Autowired
    private lateinit var extractorRepository: ExtractorRepository

    @Autowired
    private lateinit var itemDescriptorRepository: ItemDescriptorRepository

    override fun save(output: Extractor): Extractor {
        val save = extractorRepository.save(output)

        return save
    }

    override fun Map<*, *>.makeConstructorParams(orig: Any): Map<KParameter, Any?> = mapOf(
            Parameter<Extractor>("id") to this["ClassName"],
            Parameter<Extractor>("displayName") to this["mDisplayName"],
            Parameter<Extractor>("description") to this["mDescription"],
            Parameter<Extractor>("extractCycleTime") to this["mExtractCycleTime"],
            Parameter<Extractor>("itemsPerCycle") to this["mItemsPerCycle"],
            Parameter<Extractor>("powerConsumption") to this["mPowerConsumption"],
            Parameter<Extractor>("powerConsumptionExponent") to this["mPowerConsumptionExponent"],
            Parameter<Extractor>("minPotential") to this["mMinPotential"],
            Parameter<Extractor>("maxPotential") to this["mMaxPotential"],
            Parameter<Extractor>("maxPotentialIncreasePerCrystal") to this["mMaxPotentialIncreasePerCrystal"],
            Parameter<Extractor>("extractorType") to this["mExtractorTypeName"],
            Parameter<Extractor>("allowedResources") to transformAllowedResources(
                    this["mAllowedResources"] as String,
                    (this["mAllowedResourceForms"] as String).extractListEntry(),
            ).toSet()
    )

    private fun transformAllowedResources(allowedResources: String, allowedResourcesFrom: List<ItemRef>): List<String> {
        // TODO simplify with query ID = truc OR forms IN
        return allowedResources.extractListEntry().filterNot { it.isEmpty() }.map {
            val blueprintClassRegex = ".*\\.(.*)\"'".toRegex()
            val descriptor = blueprintClassRegex.matchEntire(it)?.groupValues?.get(1)
                    ?: throw Error("Could not parse $it")

            val item = itemDescriptorRepository.findById(descriptor).getOrElse { throw Error("No item descriptor ${descriptor}") }

            item.id
        }.takeIf { it.isNotEmpty() }
                ?: itemDescriptorRepository.findAllByFormInAndCategory(allowedResourcesFrom, ItemCategory.Raw).map { it.id }
    }


}