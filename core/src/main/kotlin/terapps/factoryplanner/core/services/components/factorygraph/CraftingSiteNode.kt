package terapps.factoryplanner.core.services.components.factorygraph

import terapps.factoryplanner.core.dto.CraftingMachineDto
import terapps.factoryplanner.core.dto.ItemDescriptorDto
import terapps.factoryplanner.core.dto.RecipeProducingDto
import terapps.factoryplanner.core.entities.CraftingMachineEntity

data class CraftingSiteNode(
        override val factorySiteTarget: ItemDescriptorDto,
        override var targetAmountPerCycle: Double,
        override val automaton: CraftingMachineDto,
        val recipe: RecipeProducingDto
) : AutomatedFactoryNode(FactorySiteType.CraftingSite) {

    override val id: String
        get() = recipe.className
    override val label: String
        get() = recipe.displayName

    override val produces: List<FactorySiteIO>
        get() = recipe.producing.map {
            FactorySiteIO(it.item, it.actualOutputPerCycle * recipe.manufacturingDurationByMinute)
        }

    private val targetOutputPerCycle: FactorySiteIO
        get() = produces.first { it.item.className == factorySiteTarget.className }

    override val requiredMachines: Double
        get() = targetAmountPerCycle / targetOutputPerCycle.outputPerCycle
    override val manufacturingDuration: Double
        get() = recipe.manufacturingDuration

}