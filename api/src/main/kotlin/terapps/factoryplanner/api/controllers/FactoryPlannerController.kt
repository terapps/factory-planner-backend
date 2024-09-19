package terapps.factoryplanner.api.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import terapps.factoryplanner.core.services.FactoryPlannerService
import terapps.factoryplanner.core.services.components.FactorySite
import terapps.factoryplanner.core.services.components.FactorySiteInput


@RestController
@RequestMapping("/factory-planning")
class FactoryPlannerController {
    @Autowired
    lateinit var factoryPlannerService: FactoryPlannerService

    @PostMapping
    fun factoryPlanning(
            @RequestBody factorySiteInput: FactorySiteInput,
            ): FactorySite {
        return factoryPlannerService.planFactorySite(factorySiteInput)
    }
}