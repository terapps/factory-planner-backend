package terapps.factoryplanner.services.components

import terapps.factoryplanner.entities.satisfactory.*
import kotlin.math.ceil

data class FactorySite(
        val targetDescriptor: ItemDescriptor,
        val targetRecipe: Recipe,
        val craftingMachine: CraftingMachine? = null,
        val extractor: Extractor? = null,
) {
    val destinations: MutableList<FactorySite> = mutableListOf()
    var targetAmountPerMinute: Float = 0f
    val producedItem: ItemIO
        get() = targetRecipe.produces.firstOrNull { it.isFactorySiteTargetDescriptor() }
                ?: throw Error("Target descriptor is not in produces")
    val byProducts: List<ItemIO>
        get() = targetRecipe.produces.filterNot { it.isFactorySiteTargetDescriptor() }
    val targetItemOutputPerMinute: Float
        get() = producedItem.outputPerCycle * (60f / targetRecipe.manufacturingDuration)
    val numberOfMachines: Float?
        get() = takeUnless {
            it.extractor != null
        }?.let { ceil(targetAmountPerMinute / targetItemOutputPerMinute) }
    val maxExtractorOutputOnPureNodePerMinute: Float?
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
    val nbMaxOverclockedExtractorOnPureNodes: Float?
        get() = takeIf {
            it.extractor != null
        }?.run {
            ceil(targetAmountPerMinute / maxExtractorOutputOnPureNodePerMinute!!)
        }


    private fun ItemIO.isFactorySiteTargetDescriptor() = descriptor.id == targetDescriptor.id
}