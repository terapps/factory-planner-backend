package terapps.factoryplanner.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import terapps.factoryplanner.controllers.dto.FactoryPlanningRequest
import terapps.factoryplanner.core.services.components.FactorySite
import terapps.factoryplanner.core.services.FactoryPlannerService


@RestController
class FactoryPlannerController {
    @Autowired
    lateinit var factoryPlannerService: FactoryPlannerService

    @PostMapping("/factory/plan/{itemClass}/for/{amount}")
    fun factoryPlanning(
            @PathVariable("itemClass") itemClass: String,
            @PathVariable("amount") amount: Float
            ): FactorySite {
        // TODO select mode for recipe

        val site = factoryPlannerService.planFactorySite(itemClass, amount)
        return site
    }
}