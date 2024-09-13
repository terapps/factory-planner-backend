package terapps.factoryplanner.core.projections

interface ItemIO {
    fun getDescriptor(): ItemDescriptorSummary
    fun getOutputPerCycle(): Float
}