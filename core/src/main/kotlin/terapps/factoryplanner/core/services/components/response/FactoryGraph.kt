package terapps.factoryplanner.core.services.components.response

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import terapps.factoryplanner.core.dto.ItemDescriptorDto
import terapps.factoryplanner.core.graph.GraphEdge

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = FactoryNode::class,
        visible = true
)
@JsonSubTypes(
        value = [
            JsonSubTypes.Type(value = CraftingSiteNode::class, name = "CraftingSite"),
            JsonSubTypes.Type(value = ExtractingSiteNode::class, name = "ExtractorSite"),
            JsonSubTypes.Type(value = ItemSiteNode::class, name = "ItemSite"),
        ],
)
abstract class FactoryNode(
        open val type: FactorySiteType,
) {
    abstract val factorySiteTarget: ItemDescriptorDto

    abstract val id: String
    abstract val label: String
}

data class ItemSiteNode(override val factorySiteTarget: ItemDescriptorDto) : FactoryNode(FactorySiteType.ItemSite) {
    override val id: String
        get() = factorySiteTarget.className

    override val label: String
        get() = factorySiteTarget.displayName
}


data class FactoryEdge(
        override val source: String,
        override val target: String,
        val outputPerCycle: Double? = null
) : GraphEdge
