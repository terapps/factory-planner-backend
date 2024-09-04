package terapps.factoryplanner.controllers.dto

data class FactorySiteRequest(
        val targetItemClassName: String,
        val targetAmount: Float
)

data class FactoryPlanningRequest(
        val sites: List<FactorySiteRequest>
)
