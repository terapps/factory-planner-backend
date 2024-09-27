package terapps.factoryplanner.api.services.components

import terapps.factoryplanner.core.dto.ItemDescriptorDto
import terapps.factoryplanner.core.entities.Automaton

data class FactorySiteIO(
        val item: ItemDescriptorDto,
        val outputPerCycle: Double
)

abstract class FactoryNode(
        val type: FactorySiteType,
) {
    abstract val factorySiteTarget: ItemDescriptorDto
    abstract var targetAmountPerCycle: Double
    abstract val requiredMachines: Double
    abstract val automaton: Automaton
    abstract val produces: Collection<FactorySiteIO>

    open val id: String
        get() = factorySiteTarget.className
    open val label: String
        get() = automaton.displayName
}


data class FactoryEdge(
        val output: Double,
        val source: String,
        val target: String
)

data class FactoryGraph(
        val rootSite: FactoryNode,
        val nodes: Collection<FactoryNode>,
        val edges: Collection<FactoryEdge>,
)