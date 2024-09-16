package terapps.factoryplanner.core.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import terapps.factoryplanner.core.entities.ItemCategory
import terapps.factoryplanner.core.entities.RecipeRepository
import terapps.factoryplanner.core.projections.ItemDescriptorSummary
import terapps.factoryplanner.core.projections.RecipeProducingSummary
import terapps.factoryplanner.core.projections.getActualOutputPerCycle
import terapps.factoryplanner.core.services.components.*
import terapps.factoryplanner.core.services.components.FactorySite.Companion.findInTree


@Service
class FactoryPlannerService {
    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    fun planFactorySite(factorySiteInput: FactorySiteInput): FactorySite {
        return makeFactorySite(factorySiteInput)
    }

    private fun makeFactorySite(factorySiteInput: FactorySiteInput, rootSite: FactorySite? = null): FactorySite =  when (factorySiteInput.type) {
        FactorySiteType.Extractor -> makeExtractingSite(factorySiteInput)
        FactorySiteType.Recipe -> makeMachineSite(factorySiteInput, rootSite)
    }

    private fun makeExtractingSite(factorySiteInput: FactorySiteInput): FactorySite {
        if (factorySiteInput.extractor == null) {
            throw Error("No recipe given with site ${factorySiteInput.type}")
        }
        return ExtractingSite(
                factorySiteInput.item,
                factorySiteInput.amountPerCycle,
                factorySiteInput.extractor
        )
    }

    private fun makeMachineSite(factorySiteInput: FactorySiteInput, rootSite: FactorySite? = null): CraftingMachineFactorySite {
        if (factorySiteInput.recipe == null) {
            throw Error("No recipe given with site ${factorySiteInput.type}")
        }
        val recipeProducing = recipeRepository.findByClassName(factorySiteInput.recipe.getClassName()) ?: throw Error("Cannot find recipe ${factorySiteInput.recipe.getClassName()}")

        return CraftingMachineFactorySite(factorySiteInput.item, factorySiteInput.amountPerCycle, recipeProducing).also {
            loadIngredients(factorySiteInput, it, rootSite ?: it)
        }
    }



    private fun loadIngredients(factorySiteInput: FactorySiteInput, factorySite: CraftingMachineFactorySite, rootSite: FactorySite) {
        val ingredients = factorySiteInput.ingredients

        if (ingredients.isEmpty()) {
            throw Error("No ingredients for ${factorySite.targetRecipe.getClassName()}")
        }

        ingredients.forEach { factorySiteInputIngredient ->
            val ingredientOutputPerCycle = factorySite.requiredMachines * factorySiteInputIngredient.amountPerCycle
            val ingredientTargetAmountPerCycle = ingredientOutputPerCycle * (60f / factorySite.targetRecipe.getManufacturingDuration())
            val existingSiteNode = findInTree(rootSite) { it.targetDescriptor.getClassName() == factorySiteInputIngredient.item.getClassName() }
            val receiverNode = existingSiteNode
                    ?: makeFactorySite(factorySiteInputIngredient, rootSite)

            receiverNode.destinations += Destination(factorySite.targetDescriptor.getClassName(), ingredientTargetAmountPerCycle)

            existingSiteNode?.let {
                it.targetAmountPerCycle += ingredientTargetAmountPerCycle
            } ?: run {
                factorySite.childs += receiverNode
            }
        }
    }
}