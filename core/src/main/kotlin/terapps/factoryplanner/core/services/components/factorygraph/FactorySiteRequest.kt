package terapps.factoryplanner.core.services.components.factorygraph

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = FactorySiteRequest::class,
        visible = false
)
@JsonSubTypes(
        value = [
            JsonSubTypes.Type(value = CraftingSiteRequest::class, name = "CraftingSite"),
            JsonSubTypes.Type(value = ExtractingSiteRequest::class, name = "ExtractorSite"),
            JsonSubTypes.Type(value = ItemSiteRequest::class, name = "ItemSite"),
        ],
)
abstract class FactorySiteRequest(val type: FactorySiteType) {
    abstract val itemClass: String
}

data class ItemSiteRequest(
        override val itemClass: String,
) : FactorySiteRequest(FactorySiteType.ItemSite)

data class CraftingSiteRequest(
        override val itemClass: String,
        val recipeClass: String
) : FactorySiteRequest(FactorySiteType.CraftingSite)

data class ExtractingSiteRequest(
        override val itemClass: String,
        val extractorClass: String
) : FactorySiteRequest(FactorySiteType.ExtractorSite)