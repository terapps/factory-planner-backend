package terapps.factoryplanner.api.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import terapps.factoryplanner.core.services.FactoryGraph
import terapps.factoryplanner.core.services.FactoryPlannerService
import terapps.factoryplanner.core.services.SealedGraph
import terapps.factoryplanner.core.services.components.response.FactorySiteRequest


@RestController
@RequestMapping("/factory-site")
class FactoryPlannerController {
    @Autowired
    lateinit var factoryPlannerService: FactoryPlannerService


    @PostMapping("/plan", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun planFactorySite(
            @RequestBody factorySiteRequests: Collection<FactorySiteRequest>,
            ): FactoryGraph {
        return factorySiteRequests.map {
            factoryPlannerService.planFactorySite(it)
        }.fold(FactoryGraph()) { acc, graphBuilder ->
            acc.nodes += graphBuilder.nodes
            acc.edges += graphBuilder.edges
            acc
        }.apply {
            SealedGraph(nodes.distinctBy { it.id }, edges.distinctBy { it.source to it.target })
        }
    }
}