package terapps.factoryplanner.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import terapps.factoryplanner.controllers.dto.FactoryPlanningRequest
import terapps.factoryplanner.core.services.components.FactorySite
import terapps.factoryplanner.core.services.FactoryPlannerService


@RestController
class FactoryPlannerController {
    @Autowired
    lateinit var factoryPlannerService: FactoryPlannerService

    @PostMapping("/factory/planning")

    fun factoryPlanning(
            @RequestBody factoryPlanningRequest: FactoryPlanningRequest
    ): List<FactorySite> {
        // TODO select mode for recipe
        TODO()

/*
        return this.factoryPlannerService.planFactorySite(*factoryPlanningRequest.sites.map { it.targetItemClassName to it.targetAmount }.toTypedArray())
*/
    }
}