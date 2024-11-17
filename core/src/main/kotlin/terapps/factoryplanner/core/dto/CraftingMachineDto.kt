package terapps.factoryplanner.core.dto

import terapps.factoryplanner.core.entities.Automaton
import terapps.factoryplanner.core.entities.CraftingMachineEntity
import terapps.factoryplanner.filestorage.dto.AssetsBucketEntry

class CraftingMachineDto(
        override val className: String,
        override val displayName: String,
        val description: String,
        val manufacturingSpeed: Double,
        val powerConsumption: Double,
        val powerConsumptionExponent: Double,
        override val minPotential: Double,
        override val maxPotential: Double,
        override val productionBoost: Double,
        val descriptor: ItemDescriptorDto
): Automaton {
    constructor(it: CraftingMachineEntity): this(
            it.className,
            it.displayName,
            it.description,
            it.manufacturingSpeed,
            it.powerConsumption,
            it.powerConsumptionExponent,
            it.minPotential,
            it.maxPotential,
            it.productionBoost,
            ItemDescriptorDto(it.descriptor!!)
    )

}