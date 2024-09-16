package terapps.factoryplanner.core.projections

interface RecipeIO {
    fun getItem(): ItemDescriptorSummary
    fun getOutputPerCycle(): Double
}

fun RecipeIO.getActualOutputPerCycle(): Double {
    return if (getItem().getForm() == "RF_LIQUID") getOutputPerCycle() / 1000f else getOutputPerCycle()
}