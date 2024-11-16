package terapps.factoryplanner.bootstrap.transformers.entities

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.Parameter
import terapps.factoryplanner.bootstrap.dto.SatisfactoryStaticData
import terapps.factoryplanner.bootstrap.dto.fromCategory
import terapps.factoryplanner.bootstrap.dto.generated.FGBuildableManufacturer
import terapps.factoryplanner.bootstrap.dto.generated.FGBuildableManufacturerVariablePower
import terapps.factoryplanner.bootstrap.dto.generated.FGBuildingDescriptor
import terapps.factoryplanner.bootstrap.transformers.BatchList
import terapps.factoryplanner.bootstrap.transformers.GenericAbstractTransformer
import terapps.factoryplanner.core.entities.CraftingMachineEntity
import terapps.factoryplanner.core.repositories.CraftingMachineRepository
import kotlin.reflect.KParameter

@Component
class CraftingMachineTransformer : GenericAbstractTransformer<Any, CraftingMachineEntity>(
        CraftingMachineEntity::class,
        arrayListOf(
                FGBuildableManufacturer::class,
                FGBuildableManufacturerVariablePower::class,
        )
) {
    @Autowired
    private lateinit var craftingMachineRepository: CraftingMachineRepository

    override val batch: BatchList<CraftingMachineEntity> = BatchList() {
        craftingMachineRepository.saveAll(it)
    }

    override fun Map<*, *>.makeConstructorParams(orig: Any): Map<KParameter, Any?> {


        return mapOf(
                Parameter<CraftingMachineEntity>("className") to this["ClassName"],
                Parameter<CraftingMachineEntity>("displayName") to this["mDisplayName"],
                Parameter<CraftingMachineEntity>("description") to this["mDescription"],
                Parameter<CraftingMachineEntity>("manufacturingSpeed") to this["mManufacturingSpeed"],
                Parameter<CraftingMachineEntity>("powerConsumption") to this["mPowerConsumption"],
                Parameter<CraftingMachineEntity>("powerConsumptionExponent") to this["mPowerConsumptionExponent"],
                Parameter<CraftingMachineEntity>("minPotential") to this["mMinPotential"],
                Parameter<CraftingMachineEntity>("maxPotential") to this["mMaxPotential"],
                Parameter<CraftingMachineEntity>("productionBoost") to this["mBaseProductionBoost"],
        )
    }
}
