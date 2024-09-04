package terapps.factoryplanner.bootstrap.dto

import FGAmmoTypeInstantHit
import FGAmmoTypeProjectile
import FGAmmoTypeSpreadshot
import FGBuildableManufacturer
import FGBuildableManufacturerVariablePower
import FGBuildableResourceExtractor
import FGBuildingDescriptor
import FGConsumableEquipment
import FGCustomizationRecipe
import FGPoleDescriptor
import FGRecipe
import FGSchematic
import GameEntity
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmName


data class GameObjectCategory(
        val NativeClass: String,
        val classType: KClass<*>,
        val Classes: List<GameEntity>
) {

    // TODO split probably
    // Character buildables, conveyors, pipes, crafting machines, foundations...
    fun isBuildable(): Boolean {
        return classType.simpleName!!.contains("FGBuildable") ||
                classType.isSubclassOf(FGBuildingDescriptor::class) ||
                classType.isSubclassOf(FGPoleDescriptor::class) && !isCraftingMachine() && !isExtractor()
    }

    fun isCraftingMachine(): Boolean {
        return classType.isSubclassOf(FGBuildableManufacturer::class) || classType.isSubclassOf(FGBuildableManufacturerVariablePower::class)
    }

    fun isExtractor(): Boolean {
        return classType.isSubclassOf(FGBuildableResourceExtractor::class)
    }

    // Research stuff / unlockables / hard drives
    fun isSchematic() = classType.isSubclassOf(FGSchematic::class)

    // Customization foundation, paint, patterns idgaf
    fun isCustomizationRecipe(): Boolean {
        return classType.isSubclassOf(FGCustomizationRecipe::class)
    }

    // All Recipe including character stuff
    fun isRecipe(): Boolean {
        return classType.isSubclassOf(FGRecipe::class)
    }

    fun isDescriptor(): Boolean {
        return classType.jvmName.contains(".*Descriptor.*".toRegex()) || classType.isSubclassOf(FGAmmoTypeProjectile::class) || classType.isSubclassOf(FGAmmoTypeInstantHit::class) && classType.isSubclassOf(FGAmmoTypeSpreadshot::class)
    }

    fun isConsumableEquipment(): Boolean {
        return classType.isSubclassOf(FGConsumableEquipment::class)
    }

}