package terapps.factoryplanner.core.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import terapps.factoryplanner.core.entities.ItemCategory
import terapps.factoryplanner.core.entities.RecipeRepository
import terapps.factoryplanner.core.projections.ItemDescriptorSummary
import terapps.factoryplanner.core.projections.RecipeProducingSummary
import terapps.factoryplanner.core.projections.getActualOutputPerCycle
import terapps.factoryplanner.core.services.components.CraftingMachineFactorySite
import terapps.factoryplanner.core.services.components.Destination
import terapps.factoryplanner.core.services.components.ExtractingSite
import terapps.factoryplanner.core.services.components.FactorySite


@Service
class FactoryPlannerService {
    @Autowired
    private lateinit var recipeRepository: RecipeRepository


    fun planFactorySite(itemClass: String, amount: Float): FactorySite {
        val rootSite = makeFactorySite(itemClass, amount)

        println("site: $rootSite")
        return rootSite
    }

    fun planFactorySite(selectedRecipe: RecipeProducingSummary): FactorySite {
        val targetIo = selectedRecipe.getProducing().first()

        return makeFactorySite(selectedRecipe, targetIo.getItem(), targetIo.getOutputPerCycle())
    }

    fun findInTree(factorySite: FactorySite, predicate: (it: FactorySite) -> Boolean): FactorySite? {
        return flattenFactoryTree(factorySite).firstOrNull { predicate(it) }
    }

    fun visitFactoryTree(factorySite: FactorySite, collect: (it: FactorySite) -> Unit) {
        collect(factorySite)

        for (it in factorySite.childs) {
            collect(it)
        }
    }

    fun flattenFactoryTree(factorySite: FactorySite): List<FactorySite> {
        val list = mutableListOf<FactorySite>()

        visitFactoryTree(factorySite) {
            list.add(factorySite)
        }
        return list
    }


    private fun makeFactorySite(selectedRecipe: RecipeProducingSummary, targetDescriptor: ItemDescriptorSummary, amount: Float, rootSite: FactorySite? = null): FactorySite = if (targetDescriptor.getCategory() == ItemCategory.Raw)
        makeExtractingSite(targetDescriptor, amount)
    else
        CraftingMachineFactorySite(targetDescriptor, amount, selectedRecipe).also {
            loadIngredients(it, rootSite ?: it)
        }

    private fun selectRecipe(recipes: Collection<RecipeProducingSummary>, targetDescriptorClass: String): RecipeProducingSummary {
        return recipes.filterNot {
            isByProductRecipe(it)
        }.firstOrNull() // TODO select among valid choices
                ?: throw Error("No recipe to select ${targetDescriptorClass}")
    }

    private fun isByProductRecipe(recipe: RecipeProducingSummary): Boolean {
        val produces = recipe.getProducing()

        // filter out unpackaging recipes as they cause valid graph cycle and we never want to use that as an ingredient.
        return produces.all { it.getItem().getForm() == "RF_LIQUID" || listOf("Desc_FluidCanister_C", "Desc_GasTank_C").contains(it.getItem().getClassName()) }
    }

    private fun makeFactorySite(targetDescriptorClass: String, amount: Float, rootSite: FactorySite? = null): FactorySite {
        val recipes = recipeRepository.findByProducingItemClassName(targetDescriptorClass)

        // TODO recipe selector here ~
        val selectedRecipe = selectRecipe(recipes, targetDescriptorClass)
        println("[RootSiteRecipe=${(rootSite as? CraftingMachineFactorySite)?.targetRecipe?.getClassName()}][RootSiteIngredient=${rootSite?.targetDescriptor?.getClassName()}][CurrentItemAddition=${targetDescriptorClass}]: Selected recipe ${selectedRecipe.getClassName()}")
        val targetDescriptor = selectedRecipe.getProducing().first { it.getItem().getClassName() == targetDescriptorClass }.getItem()

        return makeFactorySite(selectedRecipe, targetDescriptor, amount, rootSite)
    }

    private fun makeFactorySite(targetDescriptor: ItemDescriptorSummary, amount: Float, rootSite: FactorySite? = null): FactorySite {
        if (targetDescriptor.getCategory() == ItemCategory.Raw) {
            return makeExtractingSite(targetDescriptor, amount)
        }

        return makeFactorySite(targetDescriptor.getClassName(), amount, rootSite)
    }

    private fun makeExtractingSite(targetDescriptor: ItemDescriptorSummary, amount: Float) = ExtractingSite(
            targetDescriptor,
            amount,
            // TODO Find the right extractor with game profile (unlocks / most optimized)
            targetDescriptor.getExtractedIn().filterNot { it.className == "Build_FrackingExtractor_C" && targetDescriptor.getForm() != "RF_GAS" }.firstOrNull()
                    ?: throw Error("No extractor for raw resource ${targetDescriptor.getClassName()}"),
    )


    private fun loadIngredients(factorySite: CraftingMachineFactorySite, rootSite: FactorySite) {
        val recipeRequires = recipeRepository.findByClassName(factorySite.targetRecipe.getClassName())
                ?: throw Error("Cannot resolve recipe ${factorySite.targetRecipe.getClassName()}")
        val ingredients = recipeRequires.getIngredients()
        val produces = factorySite.targetRecipe.getProducing()

        if (ingredients.isEmpty()) {
            throw Error("No ingredients for ${factorySite.targetRecipe.getClassName()}")
        }
        if (ingredients.any { produces.map { it.getItem().getClassName() }.contains(it.getItem().getClassName()) }) {
            // TODO recycle? only if produces output != than requires output
            throw Error("Ingredient/produce cycle detected ${factorySite.targetRecipe.getClassName()}, should be able to handle them but not yet.")
        }

        ingredients.forEach { ingredient ->
            val ingredientOutputPerCycle = factorySite.requiredMachines * ingredient.getActualOutputPerCycle()
            val ingredientTargetAmountPerCycle = ingredientOutputPerCycle * (60f / factorySite.targetRecipe.getManufacturingDuration())
            val existingSiteNode = findInTree(rootSite) { it.targetDescriptor.getClassName() == ingredient.getItem().getClassName() }
            val receiverNode = existingSiteNode
                    ?: makeFactorySite(ingredient.getItem(), ingredientTargetAmountPerCycle, rootSite)

            receiverNode.destinations += Destination(factorySite.targetDescriptor.getClassName(), ingredientTargetAmountPerCycle)

            existingSiteNode?.let {
                it.targetAmountPerCycle += ingredientTargetAmountPerCycle
            } ?: run {
                factorySite.childs += receiverNode
            }
        }
    }
}