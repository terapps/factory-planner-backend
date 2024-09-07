package terapps.factoryplanner.bootstrap.transformers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.Parameter
import terapps.factoryplanner.bootstrap.dto.generated.*
import terapps.factoryplanner.core.entities.*
import kotlin.reflect.KParameter

@Component
class ItemDescriptorTransformer : GenericAbstractTransformer<Any, ItemDescriptor>(
        ItemDescriptor::class,
        arrayListOf(
                FGItemDescriptor::class,
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

    @Autowired
    private lateinit var itemDescriptorProducedByTransformer: ItemDescriptorProducedByTransformer

    override fun save(output: ItemDescriptor): ItemDescriptor = itemDescriptorRepository.save(output)

    fun attachRecipe(fgRecipe: FGRecipe, recipe: Recipe): Collection<ItemDescriptor> {
        return itemDescriptorRepository.saveAll(
                fgRecipe.mProduct.extractDictEntry().map {
                    itemDescriptorProducedByTransformer.transform(
                            recipe to it
                    )
                }
        )
    }

    override fun Map<*, *>.makeConstructorParams(orig: Any): Map<KParameter, Any?> = mapOf(
            Parameter<ItemDescriptor>("id") to this["ClassName"],
            Parameter<ItemDescriptor>("displayName") to this["mDisplayName"],
            Parameter<ItemDescriptor>("description") to this["mDescription"],
            Parameter<ItemDescriptor>("energyValue") to this["mEnergyValue"],
            Parameter<ItemDescriptor>("form") to this["mForm"],
            Parameter<ItemDescriptor>("sinkablePoints") to this["mResourceSinkPoints"],
            Parameter<ItemDescriptor>("category") to getItemCategory(orig),
    )

    private fun getItemCategory(input: Any): ItemCategory = when (input) {
        is FGItemDescriptorNuclearFuel, is FGItemDescriptor -> ItemCategory.Craftable
        is FGItemDescriptorBiomass -> ItemCategory.Biomass
        is FGResourceDescriptor -> ItemCategory.Raw
        is FGBuildingDescriptor, is FGPoleDescriptor -> ItemCategory.Building
        is FGEquipmentDescriptor, is FGAmmoTypeProjectile, is FGAmmoTypeInstantHit, is FGAmmoTypeSpreadshot -> ItemCategory.Equipment
        is FGVehicleDescriptor -> ItemCategory.Vehicle
        is FGConsumableDescriptor -> ItemCategory.Consumable
        else -> throw Error("Not supported: ${input}")
    }
}