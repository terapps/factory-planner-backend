package terapps.factoryplanner.core.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import terapps.factoryplanner.core.repositories.RecipeRepository
import terapps.factoryplanner.core.projections.RecipeProducingSummary

@Service
class RecipeService {
    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    fun findRecipeProducingBy(className: String): RecipeProducingSummary = recipeRepository.findByClassName(className) ?: throw Error("Cannot find recipe: $className")

/*
    fun findRecipeRequiring(className: String): RecipeRequiringSummary? = recipeRepository.findByClassName(className)
*/
}