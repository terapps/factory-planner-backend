package terapps.factoryplanner.core.projections

interface RecipeRequiresSummary: RecipeSummary {
    fun getIngredients(): List<RecipeIO>
}
