package terapps.factoryplanner.core.services.components

import terapps.factoryplanner.core.entities.Extractor
import terapps.factoryplanner.core.projections.ItemDescriptorSummary
import terapps.factoryplanner.core.projections.RecipeSummary

enum class FactorySiteType {
    Extractor,
    Recipe
}

data class FactorySiteInput(
        // TODO handle byproduct here?
        val type: FactorySiteType,
        val item: ItemDescriptorSummary,
        val recipe: RecipeSummary?,
        val extractor: Extractor?, // TODO summary
        val amountPerCycle: Double,
        val ingredients: Set<FactorySiteInput> = emptySet()
)