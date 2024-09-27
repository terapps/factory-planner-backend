package terapps.factoryplanner.bootstrap.steps.components

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.steps.RootStep
import terapps.factoryplanner.core.dto.RecipeProducingDto
import terapps.factoryplanner.core.dto.RecipeRequiringDto
import terapps.factoryplanner.core.entities.ItemCategory
import terapps.factoryplanner.core.services.RecipeService

typealias Branch = Map<RecipeRequiringDto, RecipeProducingDto>

data class RecipeWeight(
        val resourceWeight: Double,
        val buildingCounts: Double,
        val sinkableWeight: Double,
        val powerWeight: Double,
)

@Component
class ComputeWeightStep : RootStep {
    @Autowired
    private lateinit var recipeService: RecipeService;

    // recipe id to weight
    private val cache = mutableMapOf<String, RecipeWeight>()

    override val priority: Int
        get() = 3

    override fun prepare() {
        var leaves = resolveBranch()

        do {
            val leaveNames = computeRecipes(leaves)

            leaves = resolveBranch(leaveNames.distinct())
        } while (leaves.isNotEmpty())
    }

    override fun dispose() {
    }

    // left = requiring
    // right = producing
    private fun resolveBranch(): Branch {
        val recipesProducing = recipeService.findAllByIngredientsItemCategoryRequiring(ItemCategory.Raw)

        return recipeService.findAllByIngredientsItemCategoryProducing(ItemCategory.Raw)
                .associateWith { requiring ->
                    recipesProducing.firstOrNull { it.className == requiring.className }
                            ?: throw Error("No association producing/requiring: ${requiring.className}")
                }
    }

    private fun resolveBranch(classNames: Collection<String>): Branch {
        val recipesProducing = recipeService.findAllByIngredientsItemClassNameInProducing(classNames)

        return recipeService.findAllByIngredientsItemClassNameInRequiring(classNames)
                .associateWith { requiring ->
                    recipesProducing.firstOrNull { it.className == requiring.className }
                            ?: throw Error("No association producing/requiring: ${requiring.className}")
                }
    }

    private fun computeRecipes(recipeTree: Branch, path: MutableList<String> = mutableListOf()): Collection<String> {
        val upperLeaves = mutableListOf<String>()

        recipeTree.forEach { (requiring, producing) ->
            val cacheId = requiring.className
            val weight = cache[cacheId] ?: compute(requiring).also {
                cache[cacheId] = it
            }

            // TODO save, bulk, rel...
            upperLeaves += producing.producing.map { it.item.className }
        }

        return upperLeaves
    }

    private fun compute(recipe: RecipeRequiringDto): RecipeWeight {
        val resourceWeight = recipe.ingredients.filter { it.item.category == ItemCategory.Raw && it.item.className != "Desc_Water_C" }.sumOf {
            it.actualOutputPerCycle
        }.times(1000)

        return RecipeWeight(
                resourceWeight,
                0.0,
                0.0,
                0.0,
        )
    }
}
