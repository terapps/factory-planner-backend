package terapps.factoryplanner.api.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import terapps.factoryplanner.core.services.FactoryGraph
import terapps.factoryplanner.core.services.FactoryPlannerService
import terapps.factoryplanner.core.services.components.factorygraph.FactorySiteRequest


@RestController
@RequestMapping("/factory-site")
class FactoryPlannerController {
    @Autowired
    lateinit var factoryPlannerService: FactoryPlannerService


    @PostMapping("/plan", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun planFactorySite(
            @RequestBody factorySiteRequest: FactorySiteRequest,
            ): FactoryGraph {
        return factoryPlannerService.planFactorySite(factorySiteRequest)
    }
}