package terapps.factoryplanner.api.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import terapps.factoryplanner.api.dto.RecipeDto
import terapps.factoryplanner.api.dto.RecipeProducingDto
import terapps.factoryplanner.api.dto.RecipeRequiringDto
import terapps.factoryplanner.core.projections.RecipeProducingSummary
import terapps.factoryplanner.core.projections.RecipeRequiringSummary
import terapps.factoryplanner.core.projections.RecipeSummary
import terapps.factoryplanner.api.services.RecipeService

@RestController
@RequestMapping("/recipes")
class RecipeController {
    @Autowired
    private lateinit var recipeService: RecipeService

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findAllByProducedItem(
            @RequestParam("itemClass") itemClass: String,
    ): List<RecipeRequiringDto> {
        // TODO gametier
        return recipeService.findAllRecipesByProducingItemClassName(itemClass, 9)
    }

    @GetMapping("/{recipeClassName}/producing",produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findByClassNameProducing(
            @PathVariable("recipeClassName") recipeClassName: String,
    ): RecipeProducingDto {
        return recipeService.findRecipeProducingByClassName(recipeClassName)
    }

    @GetMapping("/{recipeClassName}/requiring", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findByClassNameRequiring(
            @PathVariable("recipeClassName") recipeClassName: String,
    ): RecipeRequiringDto {
        return recipeService.findRecipeRequiringByClassName(recipeClassName)
    }
}