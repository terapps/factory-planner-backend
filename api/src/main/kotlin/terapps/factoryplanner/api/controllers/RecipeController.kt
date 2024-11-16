package terapps.factoryplanner.api.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import terapps.factoryplanner.core.dto.RecipeProducingDto
import terapps.factoryplanner.core.dto.RecipeRequiringDto
import terapps.factoryplanner.core.services.RecipeService

@RestController
@RequestMapping("/recipes")
class RecipeController {
    @Autowired
    private lateinit var recipeService: RecipeService

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findAllByProducedItem(
            @RequestParam("itemClass") itemClass: String,
    ): Collection<RecipeRequiringDto> {
        // TODO gametier
        return recipeService.findByProducingItemClassNameIn<RecipeRequiringDto>(listOf(itemClass))
    }

    @GetMapping("/{recipeClassName}",produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findByClassName(
            @PathVariable("recipeClassName") recipeClassName: String,
    ): RecipeProducingDto {
        return recipeService.findByClassName<RecipeProducingDto>(recipeClassName)
    }


    @GetMapping("/{recipeClassName}/producing",produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findByClassNameProducing(
            @PathVariable("recipeClassName") recipeClassName: String,
    ): RecipeProducingDto {
        return recipeService.findByClassName<RecipeProducingDto>(recipeClassName)
    }

    @GetMapping("/{recipeClassName}/requiring", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findByClassNameRequiring(
            @PathVariable("recipeClassName") recipeClassName: String,
    ): RecipeRequiringDto {
        return recipeService.findByClassName<RecipeRequiringDto>(recipeClassName)
    }
}