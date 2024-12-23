package terapps.factoryplanner.bootstrap.transformers.entities

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.core.services.components.Parameter
import terapps.factoryplanner.bootstrap.dto.generated.*
import terapps.factoryplanner.bootstrap.transformers.BatchList
import terapps.factoryplanner.bootstrap.transformers.GenericAbstractTransformer
import terapps.factoryplanner.core.entities.PowerGeneratorEntity
import terapps.factoryplanner.core.repositories.PowerGeneratorRepository
import kotlin.reflect.KParameter

@Component
class PowerGeneratorTransformer : GenericAbstractTransformer<Any, PowerGeneratorEntity>(
        PowerGeneratorEntity::class,
        arrayListOf(
                FGBuildableGeneratorGeoThermal::class,
                FGBuildableGeneratorNuclear::class,
                FGBuildableGeneratorFuel::class,
                )
) {
    @Autowired
    private lateinit var powerGeneratorRepository: PowerGeneratorRepository

    override val batch: BatchList<PowerGeneratorEntity> = BatchList() {
        powerGeneratorRepository.saveAll(it)
    }

    override fun Map<*, *>.makeConstructorParams(orig: Any): Map<KParameter, Any?> {

        return mapOf(
                Parameter<PowerGeneratorEntity>("className") to this["ClassName"],
                Parameter<PowerGeneratorEntity>("displayName") to this["mDisplayName"],
                Parameter<PowerGeneratorEntity>("description") to this["mDescription"],
                Parameter<PowerGeneratorEntity>("currentPotential") to (this["m_CurrentPotential"] ?: 0.0),
                Parameter<PowerGeneratorEntity>("fuelLoadAmount") to (this["mFuelLoadAmount"] ?: 0.0),
                Parameter<PowerGeneratorEntity>("powerProduction") to (this["mPowerProduction"] ?: 0.0),
                Parameter<PowerGeneratorEntity>("minPotential") to (this["mMinPotential"]?: 0.0),
                Parameter<PowerGeneratorEntity>("maxPotential") to (this["mMaxPotential"]?: 0.0),
        )
    }
}
