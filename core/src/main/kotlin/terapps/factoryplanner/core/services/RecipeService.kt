package terapps.factoryplanner.core.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import terapps.factoryplanner.core.dto.RecipeDto
import terapps.factoryplanner.core.dto.RecipeIoDto
import terapps.factoryplanner.core.dto.RecipeProducingDto
import terapps.factoryplanner.core.dto.RecipeRequiringDto
import terapps.factoryplanner.core.projections.RecipeProducingSummary
import terapps.factoryplanner.core.projections.RecipeRequiringSummary
import terapps.factoryplanner.core.projections.RecipeSummary
import terapps.factoryplanner.core.repositories.RecipeRepository

fun RecipeSummary.toDto() = RecipeDto(
        getId(),
        getClassName(),
        getManufacturingDuration(),
        getDisplayName(),
        getManufacturedIn()
)

fun RecipeProducingSummary.toDto() = RecipeProducingDto(
        getId(),
        getClassName(),
        getManufacturingDuration(),
        getDisplayName(),
        getManufacturedIn(),
        getProducing().map { RecipeIoDto(it.getItem().toDto(), it.getOutputPerCycle()) }
)

fun RecipeRequiringSummary.toDto() = RecipeRequiringDto(
        getId(),
        getClassName(),
        getManufacturingDuration(),
        getDisplayName(),
        getManufacturedIn(),
        getIngredients().map { RecipeIoDto(it.getItem().toDto(), it.getOutputPerCycle()) }
)

@Service
class RecipeService {
    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    fun findRecipeProducingByClassName(className: String): RecipeProducingDto {
        val recipe = recipeRepository.findByClassName(className, RecipeProducingSummary::class.java)

        return recipe?.toDto() ?: throw Error("Cannot find RecipeProducing from classname: $className")
    }

    fun findRecipeRequiringByClassName(className: String): RecipeRequiringDto {
        val recipeRequiring = recipeRepository.findByClassName(className, RecipeRequiringSummary::class.java)

        return recipeRequiring?.toDto() ?: throw Error("Cannot find RecipeRequiring from classname: $className")
    }

    fun findAllRecipesByProducingItemClassName(itemClassName: String, gameTier: Int): List<RecipeDto> {
        val recipes = recipeRepository.findByProducingItemClassName(itemClassName)

        if (recipes.isEmpty()) {
            throw Error("No recipe found from item classname $itemClassName ")
        }
        return recipes.map { it.toDto() }
    }
}