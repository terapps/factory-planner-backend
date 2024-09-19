package terapps.factoryplanner.core.services.components

import terapps.factoryplanner.core.dto.ItemDescriptorDto
import terapps.factoryplanner.core.dto.RecipeProducingDto
import terapps.factoryplanner.core.entities.CraftingMachine


data class CraftingMachineFactorySite(
        override val targetDescriptor: ItemDescriptorDto,
        override var targetAmountPerCycle: Double,
        val targetRecipe: RecipeProducingDto,
) : FactorySite() {
    // TODO leftovers when ceiling up on required machine
    override val produces: List<FactorySiteIO>
        get() = targetRecipe.producing.map {
            FactorySiteIO(it.item, it.actualOutputPerCycle * (60f / targetRecipe.manufacturingDuration))
        }

    val byProducts: List<FactorySiteIO>
        get() = produces.filterNot { it.item.className == targetDescriptor.className }

    val targetOutputPerCycle: FactorySiteIO
        get() = produces.first { it.item.className == targetDescriptor.className }

    override val requiredMachines: Double
        get() = targetAmountPerCycle / targetOutputPerCycle.outputPerCycle

    override val automaton: CraftingMachine
        get() = targetRecipe.manufacturedIn.firstOrNull { !it.manual }
                ?: throw Error("No machine for ${targetRecipe}")
}