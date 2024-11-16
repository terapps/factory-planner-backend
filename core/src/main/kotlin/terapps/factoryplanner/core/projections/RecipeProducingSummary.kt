package terapps.factoryplanner.core.projections

interface RecipeProducingSummary: RecipeSummary {
    fun getProducing(): List<RecipeIoSummary>
}


