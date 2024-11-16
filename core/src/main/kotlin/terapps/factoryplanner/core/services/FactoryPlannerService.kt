package terapps.factoryplanner.core.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import terapps.factoryplanner.core.dto.ItemDescriptorDto
import terapps.factoryplanner.core.dto.RecipeProducingDto
import terapps.factoryplanner.core.dto.RecipeRequiringDto
import terapps.factoryplanner.core.entities.ItemCategory
import terapps.factoryplanner.core.graph.GraphBuilder
import terapps.factoryplanner.core.repositories.ExtractorRepository
import terapps.factoryplanner.core.services.components.factorygraph.*
import terapps.factoryplanner.core.services.components.selector.*

typealias FactoryGraph = GraphBuilder<FactoryNode, FactoryEdge>


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
        val graph = FactoryGraph()
        val item = itemDescriptorService.findByClassName(factorySiteRequest.itemClass)

        when (factorySiteRequest) {
            is ExtractingSiteRequest -> graph.makeExtractingSite(item, factorySiteRequest.targetAmountPerCycle, factorySiteRequest.extractorClass)
            is CraftingSiteRequest -> graph.makeCraftingSite(item, factorySiteRequest.targetAmountPerCycle, factorySiteRequest.recipeClass)
            is ItemSiteRequest -> graph.makeItemSite(item, factorySiteRequest.targetAmountPerCycle) // here, consider make crafting site node for each
            else -> throw Error("Unknown site type")
        }

        return graph
    }

    fun FactoryGraph.makeItemSite(item: ItemDescriptorDto, amountPerCycle: Double, loadExtra: Boolean = true): FactoryNode {
        val node = ItemSiteNode(item, amountPerCycle)

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
                makeCraftingSite(item, amountPerCycle, recipe.className, false)
            }
        }
        if (loadExtra && item.category == ItemCategory.Raw) {
            val extractors = item.extractedIn

            extractors.forEach {
                makeExtractingSite(item, amountPerCycle, it.className)
            }
        }
        return node
    }

    fun FactoryGraph.makeExtractingSite(item: ItemDescriptorDto, amountPerCycle: Double, extractorClass: String): ExtractingSiteNode {
        val extractor = extractorService.findByClassName(extractorClass) ?: throw Error("Cannot find extractor ${extractorClass}")

        val node = ExtractingSiteNode(
                item,
                amountPerCycle,
                extractor
        )
        addNode(node)
        addEdge(FactoryEdge(amountPerCycle, node.id, item.className))

        return node
    }


    fun FactoryGraph.makeCraftingSite(item: ItemDescriptorDto, amountPerCycle: Double, recipeClass: String, loadIngredients: Boolean = true): CraftingSiteNode {
        val recipeProducing = recipeService.findByClassName<RecipeProducingDto>(recipeClass)
        val node = CraftingSiteNode(
                item,
                amountPerCycle,
                recipeProducing.manufacturedIn.firstOrNull()
                        ?: throw Error("No machine ${recipeClass}"),
                recipeProducing
        )

        addNode(node)
        recipeProducing.producing.forEach {
            val outputPerCycle = node.requiredMachines * it.actualOutputPerCycle
            val producedNode = makeItemSite(it.item, outputPerCycle, false)

            addNode(producedNode)
            addEdge(FactoryEdge(outputPerCycle, node.id, producedNode.id,))
        }

        if (loadIngredients) {
            val recipeRequiring = recipeService.findByClassName<RecipeRequiringDto>(recipeClass)

            recipeRequiring.ingredients.forEach {
                val outputPerCycle = node.requiredMachines * it.actualOutputPerCycle
                val requiredNode = makeItemSite(it.item, outputPerCycle, false)

                addNode(requiredNode)
                addEdge(FactoryEdge(outputPerCycle,requiredNode.id, node.id, ))
            }
        }

        return node
    }
}