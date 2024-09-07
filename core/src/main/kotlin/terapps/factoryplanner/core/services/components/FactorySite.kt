package terapps.factoryplanner.core.services.components

import terapps.factoryplanner.core.entities.*
import kotlin.math.ceil

// TODO change datastructure to be a graph
data class FactorySite(
        val targetDescriptor: ItemDescriptor,
        val targetRecipe: Recipe,
        var targetAmountPerMinute: Float,
        val craftingMachine: CraftingMachine? = null,
        val extractor: Extractor? = null,
) {
/*    // TODO leftovers
    operator fun plus(inc: Float): FactorySite {
        targetAmountPerMinute += inc
        return this
    }

    val destinations: MutableList<FactorySite> = mutableListOf()

    val producedItem: RecipeRequires
        get() = targetRecipe.produces.firstOrNull { it.isFactorySiteTargetDescriptor() }
                ?: throw Error("Target descriptor is not in produces")
    val byProducts: List<RecipeRequires>
        get() = targetRecipe.produces.filterNot { it.isFactorySiteTargetDescriptor() }
    val targetItemOutputPerMinute: Float
        get() = producedItem.outputPerCycle * (60f / targetRecipe.manufacturingDuration)
    val numberOfMachines: Float?
        get() = takeUnless {
            it.extractor != null
        }?.let { ceil(targetAmountPerMinute / targetItemOutputPerMinute) }

    val maximumExtractionRate: Float?
        get() = takeIf {
            extractor != null
        }?.run {
            val maxPotential = extractor!!.maxPotential + (extractor.maxPotentialIncreasePerCrystal * 3f)

            val cyclePerMinute = when (extractor.id) {
                "Desc_WaterPump_C" -> {
                     extractor.extractCycleTime / maxPotential
                }
                else -> {
                    val purityMultiplier = 2f // 0.5,  1.0, 2.0

                    (extractor.extractCycleTime / maxPotential) / purityMultiplier
                }
            }
            60f / cyclePerMinute
        }

    val nbMaxedExtractors: Float?
        get() = takeIf {
            it.extractor != null
        }?.run {
            ceil(targetAmountPerMinute / maximumExtractionRate!!)
        }


    private fun RecipeRequires.isFactorySiteTargetDescriptor() = descriptor.id == targetDescriptor.id

    val weight: Float?
        get() {
            val maxExtractionRate = maximumExtractionRate

            if (maxExtractionRate == null || targetDescriptor.id == "Desc_Water_C") {
                return null
            }
            return (targetAmountPerMinute / maxExtractionRate) * 10000
        }*/
}