package terapps.factoryplanner.core.services.components.factorygraph

import terapps.factoryplanner.core.dto.CraftingMachineDto
import terapps.factoryplanner.core.dto.ItemDescriptorDto
import terapps.factoryplanner.core.dto.RecipeDto
import terapps.factoryplanner.core.dto.RecipeProducingDto
import terapps.factoryplanner.core.entities.CraftingMachineEntity

data class CraftingSiteNode(
        override val factorySiteTarget: ItemDescriptorDto,
        val automaton: CraftingMachineDto,
        val recipe: RecipeDto
) : FactoryNode(FactorySiteType.CraftingSite) {

    override val id: String
        get() = recipe.className
    override val label: String
        get() = recipe.displayName
}