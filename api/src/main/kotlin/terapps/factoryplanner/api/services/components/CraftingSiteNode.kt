package terapps.factoryplanner.api.services.components

import terapps.factoryplanner.api.dto.ItemDescriptorDto
import terapps.factoryplanner.api.dto.RecipeProducingDto
import terapps.factoryplanner.core.entities.CraftingMachine

data class CraftingSiteNode(
        override val factorySiteTarget: ItemDescriptorDto,
        override var targetAmountPerCycle: Double,
        override val automaton: CraftingMachine,
        val recipe: RecipeProducingDto
) : FactoryNode(FactorySiteType.RecipeSite) {
    override val produces: List<FactorySiteIO>
        get() = recipe.producing.map {
            FactorySiteIO(it.item, it.actualOutputPerCycle * recipe.manufacturingDurationByMinute)
        }

    private val targetOutputPerCycle: FactorySiteIO
        get() = produces.first { it.item.className == factorySiteTarget.className }

    override val requiredMachines: Double
        get() = targetAmountPerCycle / targetOutputPerCycle.outputPerCycle
}