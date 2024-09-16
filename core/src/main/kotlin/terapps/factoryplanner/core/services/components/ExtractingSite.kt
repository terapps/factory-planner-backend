package terapps.factoryplanner.core.services.components

import terapps.factoryplanner.core.entities.Extractor
import terapps.factoryplanner.core.projections.ItemDescriptorSummary

data class ExtractingSite(
        override val targetDescriptor: ItemDescriptorSummary,
        override var targetAmountPerCycle: Double,
        override val automaton: Extractor
) : FactorySite() {
    private val maxPotential = automaton.maxPotential + (automaton.productionBoost * 3f)

    val maximumExtractionRate: Double
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


    override val produces: List<FactorySiteIO>
        get() = listOf(FactorySiteIO(targetDescriptor, maximumExtractionRate))

    override val requiredMachines: Double
        get() = targetAmountPerCycle / maximumExtractionRate
}