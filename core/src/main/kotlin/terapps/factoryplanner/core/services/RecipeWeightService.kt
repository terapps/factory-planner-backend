package terapps.factoryplanner.core.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import terapps.factoryplanner.core.entities.Recipe
import terapps.factoryplanner.core.entities.RecipeRepository
import terapps.factoryplanner.core.projections.RecipeProducingSummary
import terapps.factoryplanner.core.services.components.ExtractingSite

@Service
class RecipeWeightService {
    @Autowired
    private lateinit var factoryPlannerService: FactoryPlannerService

    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    fun computeRecipeWeight(recipe: RecipeProducingSummary) {
        val factorySite = factoryPlannerService.planFactorySite(recipe)
        val rawRequirements = factoryPlannerService.flattenFactoryTree(factorySite).filter { it is ExtractingSite && it.targetDescriptor.getClassName() != "Desc_Water_C" }
        val weightPoint = rawRequirements.sumOf { (it.targetAmountPerCycle / (it as ExtractingSite).maximumExtractionRate).toDouble() * 1000 }
        var totalBuilding = 0f
        var totalSinkValue = 0f
        var totalPowerConsumption = 0f

        factoryPlannerService.visitFactoryTree(factorySite) {
            totalBuilding += it.requiredMachines
            /*
            totalSinkValue += it.targetDescriptor.getSinkablePoints()
                        totalPowerConsumption += it.requiredMachines * it.automaton
            */
        }
        recipeRepository.updateWeight(recipe.getClassName(), weightPoint.toFloat(), totalPowerConsumption, totalBuilding, totalSinkValue)
    }
}