package terapps.factoryplanner.core.services.components

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import terapps.factoryplanner.core.entities.Extractor
import terapps.factoryplanner.core.projections.ItemDescriptorSummary
import terapps.factoryplanner.core.projections.RecipeSummary

enum class FactorySiteType {
    ExtractorSite,
    RecipeSite
}

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = FactorySiteInput::class,
        visible = true
)
@JsonSubTypes(
        value = [
            JsonSubTypes.Type(value = CraftingMachineSiteInput::class, name = "RecipeSite"),
            JsonSubTypes.Type(value = ExtractingSiteInput::class, name = "ExtractorSite"),
        ],
)
abstract class FactorySiteInput(
       open val type: FactorySiteType,
       open val item: ItemDescriptorSummary,
       open val amountPerCycle: Double,
       open val ingredients: List<FactorySiteInput> = emptyList()
)

data class CraftingMachineSiteInput(
        override val item: ItemDescriptorSummary,
        override val amountPerCycle: Double,
        val recipe: RecipeSummary,
): FactorySiteInput(FactorySiteType.RecipeSite, item, amountPerCycle)

data class ExtractingSiteInput(
        override val item: ItemDescriptorSummary,
        override val amountPerCycle: Double,
        val extractor: Extractor,
): FactorySiteInput(FactorySiteType.ExtractorSite, item, amountPerCycle) {

}
