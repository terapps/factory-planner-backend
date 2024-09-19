package terapps.factoryplanner.core.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import terapps.factoryplanner.core.services.components.*
import terapps.factoryplanner.core.services.components.FactorySite.Companion.findInTree


@Service
class FactoryPlannerService {

    @Autowired
    private lateinit var recipeService: RecipeService

    fun planFactorySite(factorySiteInput: FactorySiteInput): FactorySite {
        return makeFactorySite(factorySiteInput)
    }

    private fun makeFactorySite(factorySiteInput: FactorySiteInput, rootSite: FactorySite? = null): FactorySite =  when (factorySiteInput) {
        is ExtractingSiteInput -> makeExtractingSite(factorySiteInput)
        is CraftingMachineSiteInput -> makeMachineSite(factorySiteInput, rootSite)
        else -> throw Error("Unknown site type")
    }

    private fun makeExtractingSite(factorySiteInput: ExtractingSiteInput): FactorySite {
        return ExtractingSite(
                factorySiteInput.item,
                factorySiteInput.amountPerCycle,
                factorySiteInput.extractor
        )
    }

    private fun makeMachineSite(factorySiteInput: CraftingMachineSiteInput, rootSite: FactorySite? = null): CraftingMachineFactorySite {
        val recipeProducing = recipeService.findRecipeProducingByClassName(factorySiteInput.recipe.getClassName())

        return CraftingMachineFactorySite(factorySiteInput.item, factorySiteInput.amountPerCycle, recipeProducing).also {
            loadIngredients(factorySiteInput, it, rootSite ?: it)
        }
    }



    private fun loadIngredients(factorySiteInput: FactorySiteInput, factorySite: CraftingMachineFactorySite, rootSite: FactorySite) {
        val ingredients = factorySiteInput.ingredients

        if (ingredients.isEmpty()) {
            throw Error("No ingredients for ${factorySite.targetRecipe.className}")
        }

        ingredients.forEach { factorySiteInputIngredient ->
            val ingredientOutputPerCycle = factorySite.requiredMachines * factorySiteInputIngredient.amountPerCycle
            val ingredientTargetAmountPerCycle = ingredientOutputPerCycle * (60f / factorySite.targetRecipe.manufacturingDuration)
            val existingSiteNode = findInTree(rootSite) { it.targetDescriptor.className == factorySiteInputIngredient.item.className }
            val receiverNode = existingSiteNode
                    ?: makeFactorySite(factorySiteInputIngredient, rootSite)

            receiverNode.destinations += Destination(factorySite.targetDescriptor.className, ingredientTargetAmountPerCycle)

            existingSiteNode?.let {
                it.targetAmountPerCycle += ingredientTargetAmountPerCycle
            } ?: run {
                factorySite.childs += receiverNode
            }
        }
    }
}