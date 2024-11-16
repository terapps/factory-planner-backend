package terapps.factoryplanner.core.services

import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import terapps.factoryplanner.core.dto.RecipeDto
import terapps.factoryplanner.core.dto.RecipeProducingDto
import terapps.factoryplanner.core.dto.RecipeRequiringDto
import terapps.factoryplanner.core.entities.ItemCategory
import terapps.factoryplanner.core.entities.RecipeEntity
import terapps.factoryplanner.core.projections.RecipeProducingSummary
import terapps.factoryplanner.core.projections.RecipeRequiringSummary
import terapps.factoryplanner.core.projections.RecipeSummary
import terapps.factoryplanner.core.repositories.RecipeRepository


@Service
class RecipeService {
    @Autowired
    lateinit var recipeRepository: RecipeRepository

    @Autowired
    lateinit var modelMapper: ModelMapper

    val dtoToProjection = mapOf( // TODO user modelmapper thingy
            RecipeProducingDto::class to RecipeProducingSummary::class,
            RecipeRequiringDto::class to RecipeRequiringSummary::class,
            RecipeDto::class to RecipeSummary::class,
            RecipeEntity::class to RecipeEntity::class,
    )

    final inline fun <reified T> findByClassName(className: String): T {
        val outType = this.dtoToProjection[T::class] ?: throw Error("Output type not managed: ${T::class}")
        val out = recipeRepository.findByClassName(className, outType.java) ?: throw Error("Not found")

        return modelMapper.map(out, T::class.java)
    }

    final inline fun <reified T> findByProducingItemClassNameIn(classNames: Collection<String>): Collection<T> {
        val outType = this.dtoToProjection[T::class] ?: throw Error("Output type not managed: ${T::class}")
        val out = recipeRepository.findAllByProducingItemClassNameIn(classNames, outType.java)

        return out.map { modelMapper.map(it, T::class.java) }
    }

    final inline fun <reified T> findAllByIngredientsItemCategory(category: ItemCategory): Collection<T> {
        val outType = this.dtoToProjection[T::class] ?: throw Error("Output type not managed: ${T::class}")
        val out = recipeRepository.findAllByIngredientsItemCategory(category, outType.java)

        return out.map { modelMapper.map(it, T::class.java) }
    }

    final inline fun <reified T> findAllByIngredientsItemClassNameIn(classes: Collection<String>): Collection<T> {
        val outType = this.dtoToProjection[T::class] ?: throw Error("Output type not managed: ${T::class}")
        val out = recipeRepository.findAllByIngredientsItemClassNameIn(classes, outType.java)

        return out.map { modelMapper.map(it, T::class.java) }
    }
}