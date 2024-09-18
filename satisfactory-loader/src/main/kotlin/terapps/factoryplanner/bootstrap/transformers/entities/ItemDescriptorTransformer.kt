package terapps.factoryplanner.bootstrap.transformers.entities

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.Parameter
import terapps.factoryplanner.bootstrap.dto.generated.*
import terapps.factoryplanner.bootstrap.transformers.GenericAbstractTransformer
import terapps.factoryplanner.core.entities.*
import terapps.factoryplanner.core.repositories.ItemDescriptorRepository
import java.io.File
import kotlin.reflect.KParameter

@Component
class ItemDescriptorTransformer : GenericAbstractTransformer<Any, ItemDescriptor>(
        ItemDescriptor::class,
        arrayListOf(
                FGItemDescriptor::class,
                FGItemDescriptorPowerBoosterFuel::class,
                FGPowerShardDescriptor::class,
                FGItemDescriptorBiomass::class,
                FGItemDescriptorNuclearFuel::class,
                FGResourceDescriptor::class,
                FGBuildingDescriptor::class,
                FGPoleDescriptor::class,
                FGEquipmentDescriptor::class,
                FGAmmoTypeProjectile::class,
                FGAmmoTypeInstantHit::class,
                FGAmmoTypeSpreadshot::class,
                FGVehicleDescriptor::class,
                FGConsumableDescriptor::class
        )
) {


    @Autowired
    private lateinit var itemDescriptorRepository: ItemDescriptorRepository

    override fun save(output: ItemDescriptor): ItemDescriptor {
        return itemDescriptorRepository.save(output)
    }

    override fun Map<*, *>.makeConstructorParams(orig: Any): Map<KParameter, Any?> = mapOf(
            Parameter<ItemDescriptor>("className") to this["ClassName"],
            Parameter<ItemDescriptor>("displayName") to this["mDisplayName"],
            Parameter<ItemDescriptor>("description") to this["mDescription"],
            Parameter<ItemDescriptor>("energyValue") to this["mEnergyValue"],
            Parameter<ItemDescriptor>("form") to this["mForm"],
            Parameter<ItemDescriptor>("sinkablePoints") to this["mResourceSinkPoints"],
            Parameter<ItemDescriptor>("extraPotential") to this["mExtraPotential"],
            Parameter<ItemDescriptor>("extraProductionBoost") to this["mExtraProductionBoost"],
            Parameter<ItemDescriptor>("category") to getItemCategory(orig),
            Parameter<ItemDescriptor>("iconSmall") to "${this["mSmallIcon"].toString().replace("Texture2D /Game/", "").split(".")[0]}.png",
            Parameter<ItemDescriptor>("iconPersistent") to "${this["mPersistentBigIcon"].toString().replace("Texture2D /Game/", "").split(".")[0]}.png",
            )

    private fun getItemCategory(input: Any): ItemCategory = when (input) {
        is FGItemDescriptorNuclearFuel, is FGItemDescriptor -> ItemCategory.Craftable
        is FGItemDescriptorBiomass -> ItemCategory.Biomass
        is FGResourceDescriptor -> ItemCategory.Raw
        is FGBuildingDescriptor, is FGPoleDescriptor -> ItemCategory.Building
        is FGEquipmentDescriptor, is FGAmmoTypeProjectile, is FGAmmoTypeInstantHit, is FGAmmoTypeSpreadshot -> ItemCategory.Equipment
        is FGVehicleDescriptor -> ItemCategory.Vehicle
        is FGConsumableDescriptor -> ItemCategory.Consumable
        is FGPowerShardDescriptor -> ItemCategory.PowerShard
        is FGItemDescriptorPowerBoosterFuel -> ItemCategory.Consumable
        else -> throw Error("Not supported category: ${input.javaClass.simpleName}")
    }
}