package terapps.factoryplanner.core.services.components

import terapps.factoryplanner.core.entities.CraftingMachine
import terapps.factoryplanner.core.projections.ItemDescriptorSummary
import terapps.factoryplanner.core.projections.RecipeProducingSummary
import terapps.factoryplanner.core.projections.getActualOutputPerCycle


data class CraftingMachineFactorySite(
        override val targetDescriptor: ItemDescriptorSummary,
        override var targetAmountPerCycle: Double,
        val targetRecipe: RecipeProducingSummary,
) : FactorySite() {
    // TODO leftovers when ceiling up on required machine
    override val produces: List<FactorySiteIO>
        get() = targetRecipe.getProducing().map {
            FactorySiteIO(it.getItem(), it.getActualOutputPerCycle() * (60f / targetRecipe.getManufacturingDuration()))
        }

    val byProducts: List<FactorySiteIO>
        get() = produces.filterNot { it.item.getClassName() == targetDescriptor.getClassName() }

    val targetOutputPerCycle: FactorySiteIO
        get() = produces.first { it.item.getClassName() == targetDescriptor.getClassName() }

    override val requiredMachines: Double
        get() = targetAmountPerCycle / targetOutputPerCycle.outputPerCycle

    override val automaton: CraftingMachine
        get() = targetRecipe.getManufacturedIn().firstOrNull { !it.manual }
                ?: throw Error("No machine for ${targetRecipe}")
}