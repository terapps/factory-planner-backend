package terapps.factoryplanner.core.services.components

import terapps.factoryplanner.core.entities.Automaton
import terapps.factoryplanner.core.projections.ItemDescriptorSummary
data class FactorySiteIO(
        val item: ItemDescriptorSummary,
        val outputPerCycle: Float
)

abstract class FactorySite {
    abstract val targetDescriptor: ItemDescriptorSummary
    abstract var targetAmountPerCycle: Float
    abstract val produces: List<FactorySiteIO>
    abstract val requiredMachines: Float
    abstract val automaton: Automaton
    var destinations: MutableList<Destination> = mutableListOf()
    val childs: MutableList<FactorySite> = mutableListOf()

    companion object {
        fun findInTree(factorySite: FactorySite, predicate: (it: FactorySite) -> Boolean): FactorySite? {
            return flattenFactoryTree(factorySite).firstOrNull { predicate(it) }
        }

        fun visitFactoryTree(factorySite: FactorySite, collect: (it: FactorySite) -> Unit) {
            collect(factorySite)

            for (it in factorySite.childs) {
                collect(it)
            }
        }

        fun flattenFactoryTree(factorySite: FactorySite): List<FactorySite> {
            val list = mutableListOf<FactorySite>()

            visitFactoryTree(factorySite) {
                list.add(factorySite)
            }
            return list
        }
    }
}
