package terapps.factoryplanner.api.services.components

import terapps.factoryplanner.core.services.RecipeService

class FactoryGraphBuilder(
        private val factoryGraph: FactorySiteInput,
        private val recipeService: RecipeService,
        private val nodes: MutableList<FactoryNode> = mutableListOf(),
        private val edges: MutableList<FactoryEdge> = mutableListOf()
) {
    fun build(): FactoryGraph  {
        val rootSite = makeFactorySite(factoryGraph)

        return FactoryGraph(
                rootSite,
                nodes,
                edges
        )
    }

    private fun makeFactorySite(factorySiteInput: FactorySiteInput): FactoryNode {
        return when (factorySiteInput) {
            is ExtractingSiteInput -> makeExtractingSite(factorySiteInput)
            is CraftingMachineSiteInput -> makeMachineSite(factorySiteInput)
            else -> throw Error("Unknown site type")
        }
    }

    private fun makeExtractingSite(factorySiteInput: ExtractingSiteInput, parent: FactoryNode? = null): ExtractingSiteNode = ExtractingSiteNode(
            factorySiteInput.item,
            factorySiteInput.amountPerCycle,
            factorySiteInput.extractor
    ).also { node ->
        registerNode(node)

        parent?.let {
            registerEdges(node, parent)
        }
    }


    private fun makeMachineSite(factorySiteInput: CraftingMachineSiteInput): CraftingSiteNode {
        val recipeProducing = recipeService.findRecipeProducingByClassName(factorySiteInput.recipe.className)

        return CraftingSiteNode(
                factorySiteInput.item,
                factorySiteInput.amountPerCycle,
                factorySiteInput.recipe.manufacturedIn.firstOrNull()
                        ?: throw Error("No machine ${factorySiteInput.recipe}"),
                recipeProducing
        ).also {
            registerNode(it)
            loadIngredients(factorySiteInput, it)
        }
    }

    private fun registerNode(node: FactoryNode) {
        nodes += node
    }

    private fun registerEdges(source: FactoryNode, destination: FactoryNode) {
        edges += source.produces.map {
            FactoryEdge(
                    it.outputPerCycle,
                    it.item.className,
                    destination.id
            )
        }
    }

    private fun loadIngredients(factorySiteInput: FactorySiteInput, parent: CraftingSiteNode) {
        val ingredients = factorySiteInput.ingredients

        if (ingredients.isEmpty()) {
            throw Error("No ingredients for ${parent.recipe.className}")
        }

        ingredients.forEach { factorySiteInputIngredient ->
            val ingredientOutputPerCycle = parent.requiredMachines * factorySiteInputIngredient.amountPerCycle
            val ingredientTargetAmountPerCycle = ingredientOutputPerCycle * parent.recipe.manufacturingDurationByMinute
            val existingIngredientNode = nodes.find { factoryNode -> factoryNode.produces.any { factorySiteInputIngredient.item.className == it.item.className } }
            val ingredientNode = existingIngredientNode ?: makeFactorySite(factorySiteInputIngredient)

            registerEdges(ingredientNode, parent)

            existingIngredientNode?.let {
                it.targetAmountPerCycle += ingredientTargetAmountPerCycle
            }
        }
    }
}