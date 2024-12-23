package terapps.factoryplanner.core.services.components.response

import terapps.factoryplanner.core.dto.CraftingMachineDto
import terapps.factoryplanner.core.dto.ItemDescriptorDto
import terapps.factoryplanner.core.dto.RecipeDto

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