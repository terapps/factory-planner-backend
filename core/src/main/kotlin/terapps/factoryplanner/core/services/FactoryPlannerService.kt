package terapps.factoryplanner.core.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import terapps.factoryplanner.core.dto.*
import terapps.factoryplanner.core.entities.ItemCategory
import terapps.factoryplanner.core.graph.Graph
import terapps.factoryplanner.core.graph.GraphBuilder
import terapps.factoryplanner.core.repositories.ExtractorRepository
import terapps.factoryplanner.core.services.components.factorygraph.*
import terapps.factoryplanner.core.services.components.selector.COMPACTED_COAL
import terapps.factoryplanner.core.services.components.selector.IRON_INGOT
import terapps.factoryplanner.core.services.components.selector.ITEM_IS_GAS
import terapps.factoryplanner.core.services.components.selector.ITEM_IS_LIQUID

typealias FactoryGraphBuilder = GraphBuilder<FactoryNode, FactoryEdge>
typealias FactoryGraph = Graph<FactoryNode, FactoryEdge>

@Service
class FactoryPlannerService {
    @Autowired
    private lateinit var recipeService: RecipeService

    @Autowired
    private lateinit var itemDescriptorService: ItemDescriptorService

    @Autowired
    private lateinit var extractorService: ExtractorRepository // TODO use service that doesnt exist at the time i write

    companion object {

        /**
         * Item to Recipe pattern exclusion
         */

        val producerExclusion = mapOf(
                IRON_INGOT to "Recipe_Alternate_IronIngot_Leached_C".toRegex(),
                ITEM_IS_LIQUID to ".*package.*".toRegex(),
                ITEM_IS_GAS to ".*package.*".toRegex(),
        )

        /**
         * Item to Recipe pattern inclusion
         */

        val producerForcedRecipe = mapOf(
                COMPACTED_COAL to "Recipe_Alternate_EnrichedCoal_C".toRegex(),
        )
    }

    fun planFactorySite(factorySiteRequest: FactorySiteRequest): FactoryGraph {
        val graph = FactoryGraphBuilder()
        val item = itemDescriptorService.findByClassName(factorySiteRequest.itemClass)

        when (factorySiteRequest) {
            is ExtractingSiteRequest -> graph.makeExtractingSite(item, factorySiteRequest.extractorClass)
            is CraftingSiteRequest -> graph.makeCraftingSite(item, factorySiteRequest.recipeClass)
            is ItemSiteRequest -> graph.makeItemSite(item)
            else -> throw Error("Unknown site type")
        }

        return graph.seal()
    }

    fun FactoryGraphBuilder.makeItemSite(item: ItemDescriptorDto, loadExtra: Boolean = true): FactoryNode {
        val node = ItemSiteNode(item)

        addNode(node)

        if (loadExtra) {
            val exclusionMatchers = producerExclusion.filter { (predicate, _) ->
                predicate.compute(item)
            }.map { (_, regex) -> regex }
            val inclusionMatchers = producerForcedRecipe.filter { (predicate, _) ->
                predicate.compute(item)
            }.map { (_, regex) -> regex }

            val recipes = recipeService.findByProducingItemClassNameIn<RecipeRequiringDto>(listOf(item.className)).filterNot {
                exclusionMatchers.any { matcher -> matcher.matches(it.className) }
            }.filter {
                inclusionMatchers.isEmpty() || inclusionMatchers.any { matcher -> matcher.matches(it.className) }
            }

            recipes.forEach { recipe ->
                makeCraftingSite(item, recipe.className, recipe)
            }
        }
        if (loadExtra && item.category == ItemCategory.Raw) {
            val extractors = item.extractedIn

            extractors.forEach {
                makeExtractingSite(item, it.className)
            }
        }
        return node
    }

    fun FactoryGraphBuilder.makeExtractingSite(item: ItemDescriptorDto, extractorClass: String): ExtractingSiteNode {
        val extractor = extractorService.findByClassName(extractorClass)
                ?: throw Error("Cannot find extractor ${extractorClass}")

        val node = ExtractingSiteNode(
                item,
                ExtractorDto(extractor),
        )
        addNode(node)
        addEdge(FactoryEdge(node.id, item.className))

        return node
    }


    fun FactoryGraphBuilder.makeCraftingSite(item: ItemDescriptorDto, recipeClass: String, recipeRequiring: RecipeRequiringDto? = null): CraftingSiteNode {
        val recipeProducing = recipeService.findByClassName<RecipeProducingDto>(recipeClass)
        val ingredientsRecipe = recipeRequiring ?: recipeService.findByClassName<RecipeRequiringDto>(recipeClass)
        val craftingMachine = recipeProducing.manufacturedIn.firstOrNull() ?: let {
            val workshopItem = itemDescriptorService.findByClassName("Desc_Workshop_C")

            CraftingMachineDto("Desc_Workshop_C", "Manual crafting", "Non automated craft", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, workshopItem)
        }


        val node = CraftingSiteNode(
                item,
                craftingMachine,
                recipeProducing
        )

        addNode(node)
        recipeProducing.producing.forEach {
            val producedNode = makeItemSite(it.item, false)

            addNode(producedNode)
            addEdge(FactoryEdge(node.id, producedNode.id, it.actualOutputPerCycle))
        }

        ingredientsRecipe.ingredients.forEach {
            val requiredNode = makeItemSite(it.item, false)

            addNode(requiredNode)
            addEdge(FactoryEdge(requiredNode.id, node.id, it.actualOutputPerCycle))
        }

        return node
    }
}