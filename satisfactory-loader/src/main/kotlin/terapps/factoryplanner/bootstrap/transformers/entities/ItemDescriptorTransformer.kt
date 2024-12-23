package terapps.factoryplanner.bootstrap.transformers.entities

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.core.services.components.Parameter
import terapps.factoryplanner.bootstrap.dto.generated.*
import terapps.factoryplanner.bootstrap.transformers.BatchList
import terapps.factoryplanner.bootstrap.transformers.GenericAbstractTransformer
import terapps.factoryplanner.core.entities.*
import terapps.factoryplanner.core.repositories.ItemDescriptorRepository
import kotlin.reflect.KParameter

@Component
class ItemDescriptorTransformer : GenericAbstractTransformer<Any, ItemDescriptorEntity>(
        ItemDescriptorEntity::class,
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

    override val batch: BatchList<ItemDescriptorEntity> = BatchList() {
        itemDescriptorRepository.saveAll(it)
    }

    override fun Map<*, *>.makeConstructorParams(orig: Any): Map<KParameter, Any?> {
        val displayName = (this["mDisplayName"] as? String)?.takeIf { it.isNotEmpty() } ?: (this["ClassName"] as String).run {
            this.split("_").get(1)
        }
        return mapOf(
                Parameter<ItemDescriptorEntity>("className") to this["ClassName"],
                Parameter<ItemDescriptorEntity>("displayName") to displayName,
                Parameter<ItemDescriptorEntity>("description") to this["mDescription"],
                Parameter<ItemDescriptorEntity>("energyValue") to this["mEnergyValue"],
                Parameter<ItemDescriptorEntity>("form") to this["mForm"],
                Parameter<ItemDescriptorEntity>("sinkablePoints") to this["mResourceSinkPoints"],
                Parameter<ItemDescriptorEntity>("extraPotential") to this["mExtraPotential"],
                Parameter<ItemDescriptorEntity>("extraProductionBoost") to this["mExtraProductionBoost"],
                Parameter<ItemDescriptorEntity>("category") to getItemCategory(orig),
                Parameter<ItemDescriptorEntity>("iconSmall") to "FactoryGame/Content/${this["mSmallIcon"].toString().replace("Texture2D /Game/", "").split(".")[0]}.png",
                Parameter<ItemDescriptorEntity>("iconPersistent") to "FactoryGame/Content/${this["mPersistentBigIcon"].toString().replace("Texture2D /Game/", "").split(".")[0]}.png",
        )
    }

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