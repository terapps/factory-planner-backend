package terapps.factoryplanner.core.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import terapps.factoryplanner.core.repositories.RecipeRepository
import terapps.factoryplanner.core.projections.RecipeProducingSummary
import terapps.factoryplanner.core.projections.RecipeRequiringSummary
import terapps.factoryplanner.core.projections.RecipeSummary

@Service
class RecipeService {
    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    fun findRecipeProducingByClassName(className: String): RecipeProducingSummary = recipeRepository.findByClassName(className, RecipeProducingSummary::class.java) ?: throw Error("Cannot find RecipeProducing from classname: $className")

    fun findRecipeRequiringByClassName(className: String): RecipeRequiringSummary = recipeRepository.findByClassName(className, RecipeRequiringSummary::class.java) ?: throw Error("Cannot find RecipeRequiring from classname: $className")

    fun findAllRecipesByProducingItemClassName(itemClassName: String, gameTier: Int): Collection<RecipeSummary> {
        val recipes = recipeRepository.findByProducingItemClassName(itemClassName)

        if (recipes.isEmpty()) {
            throw Error("No recipe found from item classname $itemClassName ")
        }
        return recipes
    }
}