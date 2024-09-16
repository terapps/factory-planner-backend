package terapps.factoryplanner.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import terapps.factoryplanner.core.projections.RecipeProducingSummary
import terapps.factoryplanner.core.projections.RecipeRequiringSummary
import terapps.factoryplanner.core.projections.RecipeSummary
import terapps.factoryplanner.core.services.FactoryPlannerService
import terapps.factoryplanner.core.services.RecipeService
import terapps.factoryplanner.core.services.components.FactorySite

@RestController
@RequestMapping("/recipes")
class RecipeController {
    @Autowired
    lateinit var recipeService: RecipeService

    @GetMapping
    fun findAllByProducedItem(
            @RequestParam("itemClass") itemClass: String,
    ): Collection<RecipeSummary> {
        // TODO gametier
        return recipeService.findAllRecipesByProducingItemClassName(itemClass, 9)
    }

    @GetMapping("/{recipeClassName}/producing")
    fun findByClassNameProducing(
            @PathVariable("recipeClassName") recipeClassName: String,
    ): RecipeProducingSummary {
        return recipeService.findRecipeProducingByClassName(recipeClassName)
    }

    @GetMapping("/{recipeClassName}/requiring")
    fun findByClassNameRequiring(
            @PathVariable("recipeClassName") recipeClassName: String,
    ): RecipeRequiringSummary {
        return recipeService.findRecipeRequiringByClassName(recipeClassName)
    }
}