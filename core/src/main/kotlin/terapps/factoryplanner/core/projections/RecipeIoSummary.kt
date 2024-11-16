package terapps.factoryplanner.core.projections

interface RecipeIoSummary {
    fun getItem(): ItemDescriptorSummary
    fun getOutputPerCycle(): Double
}

fun RecipeIoSummary.getActualOutputPerCycle(): Double {
    return if (getItem().getForm() == "RF_LIQUID") getOutputPerCycle() / 1000f else getOutputPerCycle()
}