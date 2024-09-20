package terapps.factoryplanner.api.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import terapps.factoryplanner.api.services.components.*


@Service
class FactoryPlannerService {

    @Autowired
    private lateinit var recipeService: RecipeService

    fun planFactorySite(factorySiteInput: FactorySiteInput): FactoryGraph {
        return FactoryGraphBuilder(factorySiteInput, recipeService).build()
    }
}