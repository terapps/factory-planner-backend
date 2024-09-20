package terapps.factoryplanner.api.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import terapps.factoryplanner.api.services.FactoryPlannerService
import terapps.factoryplanner.api.services.components.FactoryGraph
import terapps.factoryplanner.api.services.components.FactorySiteInput


@RestController
@RequestMapping("/factory-planning")
class FactoryPlannerController {
    @Autowired
    lateinit var factoryPlannerService: FactoryPlannerService

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun factoryPlanning(
            @RequestBody factorySiteInput: FactorySiteInput,
            ): FactoryGraph {
        return factoryPlannerService.planFactorySite(factorySiteInput)
    }
}