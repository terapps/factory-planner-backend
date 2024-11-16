package terapps.factoryplanner.core.projections

interface RecipeRequiringSummary: RecipeSummary {
    fun getIngredients(): List<RecipeIoSummary>
}
