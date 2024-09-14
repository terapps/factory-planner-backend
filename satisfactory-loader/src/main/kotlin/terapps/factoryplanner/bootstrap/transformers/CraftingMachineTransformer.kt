package terapps.factoryplanner.bootstrap.transformers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.Parameter
import terapps.factoryplanner.bootstrap.dto.generated.FGBuildableManufacturer
import terapps.factoryplanner.bootstrap.dto.generated.FGBuildableManufacturerVariablePower
import terapps.factoryplanner.core.entities.CraftingMachine
import terapps.factoryplanner.core.entities.CraftingMachineRepository
import kotlin.reflect.KParameter

@Component
class CraftingMachineTransformer : GenericAbstractTransformer<Any, CraftingMachine>(
        CraftingMachine::class,
        arrayListOf(
                FGBuildableManufacturer::class,
                FGBuildableManufacturerVariablePower::class,
        )
) {


    @Autowired
    private lateinit var craftingMachineRepository: CraftingMachineRepository

    override fun save(output: CraftingMachine): CraftingMachine = craftingMachineRepository.save(output)

    override fun Map<*, *>.makeConstructorParams(orig: Any): Map<KParameter, Any?> = mapOf(
            Parameter<CraftingMachine>("className") to this["ClassName"],
            Parameter<CraftingMachine>("displayName") to this["mDisplayName"],
            Parameter<CraftingMachine>("description") to this["mDescription"],
            Parameter<CraftingMachine>("manufacturingSpeed") to this["mManufacturingSpeed"],
            Parameter<CraftingMachine>("powerConsumption") to this["mPowerConsumption"],
            Parameter<CraftingMachine>("powerConsumptionExponent") to this["mPowerConsumptionExponent"],
            Parameter<CraftingMachine>("minPotential") to this["mMinPotential"],
            Parameter<CraftingMachine>("maxPotential") to this["mMaxPotential"],
            Parameter<CraftingMachine>("productionBoost") to this["mBaseProductionBoost"],
    )
}