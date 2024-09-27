package terapps.factoryplanner.core.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import terapps.factoryplanner.core.dto.RecipeDto
import terapps.factoryplanner.core.dto.RecipeIoDto
import terapps.factoryplanner.core.dto.RecipeProducingDto
import terapps.factoryplanner.core.dto.RecipeRequiringDto
import terapps.factoryplanner.core.entities.ItemCategory
import terapps.factoryplanner.core.projections.RecipeProducingSummary
import terapps.factoryplanner.core.projections.RecipeRequiringSummary
import terapps.factoryplanner.core.projections.RecipeSummary
import terapps.factoryplanner.core.repositories.RecipeRepository

fun Any.toDto() = when (this) { // TODO bad design
    is RecipeRequiringSummary  -> this.toDto()
    is RecipeProducingSummary  -> this.toDto()
    is RecipeSummary -> this.toDto()
    else -> throw Error("Unknown converter")
}

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
        println(recipeRequiring?.getIngredients())

        return recipeRequiring?.toDto() ?: throw Error("Cannot find RecipeRequiring from classname: $className")
    }

    fun findAllRecipesByProducingItemClassName(itemClassName: String, gameTier: Int): Collection<RecipeRequiringDto> {
        val recipes = recipeRepository.findByProducingItemClassName(itemClassName, RecipeRequiringSummary::class.java)

        if (recipes.isEmpty()) {
            throw Error("No recipe found from item classname $itemClassName ")
        }
        return recipes.map { it.toDto() }
    }

    fun findAllByIngredientsItemCategoryProducing(category: ItemCategory): Collection<RecipeRequiringDto> {
        val recipes = recipeRepository.findAllByIngredientsItemCategory(category, RecipeRequiringSummary::class.java)

        return recipes.map { it.toDto() }
    }

    fun findAllByIngredientsItemCategoryRequiring(category: ItemCategory): Collection<RecipeProducingDto> {
        val recipes = recipeRepository.findAllByIngredientsItemCategory(category, RecipeProducingSummary::class.java)

        return recipes.map { it.toDto() }
    }

    fun findAllByIngredientsItemClassNameInProducing(classes: Collection<String>): Collection<RecipeProducingDto> {
        val recipes = recipeRepository.findAllByIngredientsItemClassNameIn(classes, RecipeProducingSummary::class.java)

        return recipes.map { it.toDto() }
    }

    fun findAllByIngredientsItemClassNameInRequiring(classes: Collection<String>): Collection<RecipeRequiringDto> {
        val recipes = recipeRepository.findAllByIngredientsItemClassNameIn(classes, RecipeRequiringSummary::class.java)

        return recipes.map { it.toDto() }
    }

    fun findAllByProducingItemClassNameInRequiring(classes: Collection<String>): Collection<RecipeRequiringDto> {
        val recipes = recipeRepository.findAllByProducingItemClassNameIn(classes, RecipeRequiringSummary::class.java)

        return recipes.map { it.toDto() }
    }
}