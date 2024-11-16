package terapps.factoryplanner.core.services.components.factorygraph

import terapps.factoryplanner.core.dto.ItemDescriptorDto
import terapps.factoryplanner.core.entities.ExtractorEntity

data class ExtractingSiteNode(
        override val factorySiteTarget: ItemDescriptorDto,
        override var targetAmountPerCycle: Double,
        override val automaton: ExtractorEntity
) : AutomatedFactoryNode(FactorySiteType.CraftingSite) {
    override val label: String
        get() = "${factorySiteTarget.displayName}-${automaton.displayName}"

    override val id: String
        get() = "${factorySiteTarget.className}-${automaton.className}"

    private val maxPotential = automaton.maxPotential + (automaton.productionBoost * 3f)
    private val maximumExtractionRate: Double
        get() {

            val cyclePerMinute = when (automaton.className) {
                "Desc_WaterPump_C" -> {
                    automaton.extractCycleTime / maxPotential
                }

                else -> {
                    val purityMultiplier = 2f // 0.5,  1.0, 2.0

                    (automaton.extractCycleTime / maxPotential) / purityMultiplier
                }
            }

            return 60f / cyclePerMinute
        }

    override val requiredMachines: Double
        get() = targetAmountPerCycle / maximumExtractionRate
    override val manufacturingDuration: Double
        get() = automaton.extractCycleTime

    override val produces: Collection<FactorySiteIO>
        get() = listOf(FactorySiteIO(factorySiteTarget, targetAmountPerCycle))
}