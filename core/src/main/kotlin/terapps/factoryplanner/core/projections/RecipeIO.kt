package terapps.factoryplanner.core.projections

interface RecipeIO {
    fun getItem(): ItemDescriptorSummary
    fun getOutputPerCycle(): Float
}

fun RecipeIO.getActualOutputPerCycle(): Float {
    return if (getItem().getForm() == "RF_LIQUID") getOutputPerCycle() / 1000f else getOutputPerCycle()
}