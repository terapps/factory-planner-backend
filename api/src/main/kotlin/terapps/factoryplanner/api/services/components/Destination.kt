package terapps.factoryplanner.api.services.components

data class Destination(
        val factorySiteId: String,
        val targetAmountPerCycle: Double,
) {
}