package terapps.factoryplanner.core.dto

import terapps.factoryplanner.core.entities.PowerGeneratorEntity

class PowerGeneratorFuelRequires(
        val item: ItemDescriptorDto,
        val burnTime: Double,
        val inputPerCycle: Double,
)

class PowerGeneratorDto(
        val className: String,
        val currentPotential: Double,
        val fuelLoadAmount: Double,
        val powerProduction: Double,
        val fuel: Set<PowerGeneratorFuelRequires>,
        val supplementalResource: ItemIoDto? = null,
        val byproducts: ItemIoDto? = null,
) {
    constructor(entity: PowerGeneratorEntity) : this(entity.className, entity.currentPotential, entity.fuelLoadAmount, entity.powerProduction,
            entity.fuel.map {
                PowerGeneratorFuelRequires(ItemDescriptorDto(it.item), it.burnTime, it.inputPerCycle)
            }.toSet(),
            entity.supplementalResource?.let {
                ItemIoDto(ItemDescriptorDto(it.item), it.outputPerCycle)
            },
            entity.byproducts?.let {
                ItemIoDto(ItemDescriptorDto(it.item), it.outputPerCycle)
            })
}

