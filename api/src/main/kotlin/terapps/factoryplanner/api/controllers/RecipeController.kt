package terapps.factoryplanner.api.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import terapps.factoryplanner.core.dto.RecipeDto
import terapps.factoryplanner.core.dto.RecipeProducingDto
import terapps.factoryplanner.core.dto.RecipeRequiringDto
import terapps.factoryplanner.core.projections.RecipeProducingSummary
import terapps.factoryplanner.core.projections.RecipeRequiringSummary
import terapps.factoryplanner.core.projections.RecipeSummary
import terapps.factoryplanner.core.services.RecipeService

@RestController
@RequestMapping("/recipes")
class RecipeController {
    @Autowired
    private lateinit var recipeService: RecipeService

    @GetMapping
    fun findAllByProducedItem(
            @RequestParam("itemClass") itemClass: String,
    ): List<RecipeDto> {
        // TODO gametier
        return recipeService.findAllRecipesByProducingItemClassName(itemClass, 9)
    }

    @GetMapping("/{recipeClassName}/producing")
    fun findByClassNameProducing(
            @PathVariable("recipeClassName") recipeClassName: String,
    ): RecipeProducingDto {
        return recipeService.findRecipeProducingByClassName(recipeClassName)
    }

    @GetMapping("/{recipeClassName}/requiring")
    fun findByClassNameRequiring(
            @PathVariable("recipeClassName") recipeClassName: String,
    ): RecipeRequiringDto {
        return recipeService.findRecipeRequiringByClassName(recipeClassName)
    }
}