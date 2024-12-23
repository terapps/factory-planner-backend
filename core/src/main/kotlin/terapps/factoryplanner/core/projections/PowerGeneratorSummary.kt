package terapps.factoryplanner.core.projections

interface PowerGeneratorSummary {
    fun getClassName(): String
    fun getDisplayName(): String
    fun getDescription(): String
    fun getCurrentPotential(): Double
    fun getFuelLoadAmount(): Double
    fun getPowerProduction(): Double
    fun getMinPotential(): Double
    fun getMaxPotential(): Double
}