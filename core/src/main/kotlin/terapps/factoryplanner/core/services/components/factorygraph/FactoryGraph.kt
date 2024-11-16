package terapps.factoryplanner.core.services.components.factorygraph

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import terapps.factoryplanner.core.dto.ItemDescriptorDto
import terapps.factoryplanner.core.entities.Automaton
import terapps.factoryplanner.core.graph.GraphEdge


data class FactorySiteIO(
        val item: ItemDescriptorDto,
        val outputPerCycle: Double
)

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
    abstract var targetAmountPerCycle: Double

    abstract val id: String
    abstract val label: String
}

data class ItemSiteNode(override val factorySiteTarget: ItemDescriptorDto, override var targetAmountPerCycle: Double): FactoryNode(FactorySiteType.ItemSite) {
    override val id: String
        get() = factorySiteTarget.className

    override val label: String
        get() = factorySiteTarget.displayName
}

abstract class AutomatedFactoryNode(
        override val type: FactorySiteType,
): FactoryNode(type) {
    abstract val requiredMachines: Double
    abstract val manufacturingDuration: Double
    abstract val automaton: Automaton
    abstract val produces: Collection<FactorySiteIO>

    open val cyclePerMinute: Double
        get() =  60.0 / manufacturingDuration
    open val targetOutputPerMinute: Double
        get() = targetAmountPerCycle * cyclePerMinute
}

data class FactoryEdge(
        val outputPerCycle: Double,
        override val source: String,
        override val target: String
): GraphEdge
