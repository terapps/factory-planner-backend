package terapps.factoryplanner.bootstrap.transformers

import FGAmmoTypeInstantHit
import FGAmmoTypeProjectile
import FGAmmoTypeSpreadshot
import FGItemDescriptor
import FGItemDescriptorBiomass
import FGItemDescriptorNuclearFuel
import FGBuildingDescriptor
import FGConsumableDescriptor
import FGEquipmentDescriptor

import FGPoleDescriptor
import FGResourceDescriptor
import FGVehicleDescriptor
import terapps.factoryplanner.core.entities.ItemCategory
import terapps.factoryplanner.core.entities.ItemDescriptor

fun FGItemDescriptor.toItemDescriptor() = ItemDescriptor(
        id = ClassName,
        displayName = mDisplayName,
        description = mDescription,
        energyValue = mEnergyValue,
        form = mForm,
        sinkablePoints = mResourceSinkPoints,
        category = ItemCategory.Craftable
)

fun FGItemDescriptorBiomass.toItemDescriptor() = ItemDescriptor(
        id = ClassName,
        displayName = mDisplayName,
        description = mDescription,
        energyValue = mEnergyValue,
        form = mForm,
        sinkablePoints = mResourceSinkPoints,
        category = ItemCategory.Biomass

)

fun FGItemDescriptorNuclearFuel.toItemDescriptor() = ItemDescriptor(
        id = ClassName,
        displayName = mDisplayName,
        description = mDescription,
        energyValue = mEnergyValue,
        form = mForm,
        sinkablePoints = mResourceSinkPoints,
        category = ItemCategory.Craftable
)


fun FGResourceDescriptor.toItemDescriptor() = ItemDescriptor(
        id = ClassName,
        displayName = mDisplayName,
        description = mDescription,
        form = mForm,
        sinkablePoints = mResourceSinkPoints,
        category = ItemCategory.Raw,
)

fun FGBuildingDescriptor.toItemDescriptor() = ItemDescriptor(
        id = ClassName,
        displayName = mDisplayName,
        description = mDescription,
        form = mForm,
        category = ItemCategory.Building,
)
fun FGPoleDescriptor.toItemDescriptor() = ItemDescriptor(
        id = ClassName,
        displayName = mDisplayName,
        description = mDescription,
        form = mForm,
        category = ItemCategory.Building,
)

fun FGEquipmentDescriptor.toItemDescriptor() = ItemDescriptor(
        id = ClassName,
        displayName = mDisplayName,
        description = mDescription,
        form = mForm,
        category = ItemCategory.Equipment,
)

fun FGAmmoTypeProjectile.toItemDescriptor() = ItemDescriptor(
        id = ClassName,
        displayName = mDisplayName,
        description = mDescription,
        form = mForm,
        category = ItemCategory.Equipment,
)

fun FGAmmoTypeInstantHit.toItemDescriptor() = ItemDescriptor(
        id = ClassName,
        displayName = mDisplayName,
        description = mDescription,
        form = mForm,
        category = ItemCategory.Equipment,
)

fun FGAmmoTypeSpreadshot.toItemDescriptor() = ItemDescriptor(
        id = ClassName,
        displayName = mDisplayName,
        description = mDescription,
        form = mForm,
        category = ItemCategory.Equipment,
)

fun FGVehicleDescriptor.toItemDescriptor() = ItemDescriptor(
        id = ClassName,
        displayName = mDisplayName,
        description = mDescription,
        form = mForm,
        category = ItemCategory.Vehcle,
)

fun FGConsumableDescriptor.toItemDescriptor() = ItemDescriptor(
        id = ClassName,
        displayName = mDisplayName,
        description = mDescription,
        form = mForm,
        category = ItemCategory.Consumable,
)
