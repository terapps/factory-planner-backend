package terapps.factoryplanner.core.services

import org.neo4j.cypherdsl.core.renderer.Configuration
import org.neo4j.cypherdsl.core.renderer.Dialect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import terapps.factoryplanner.core.entities.ItemDescriptorRepository
import terapps.factoryplanner.core.entities.RecipeRepository
import terapps.factoryplanner.core.services.components.FactoryRequirementBuilder
import terapps.factoryplanner.core.services.components.FactorySite


@Service
class FactoryPlannerService {
    @Autowired
    lateinit var recipeRepository: RecipeRepository

    @Autowired
    lateinit var itemDescriptorRepository: ItemDescriptorRepository


/*
    fun planFactorySite(vararg requirements:  Pair<String, Float>): List<FactorySite> {
        val builder = FactoryRequirementBuilder.newBuilder(recipeRepository, itemDescriptorRepository)

        for (item in requirements) {
            builder.addRequirement(item.first, item.second)
        }
        val req = builder.build()

        for (site in req) {
            println("Site: ${site.targetDescriptor.id}, Recipe : ${site.targetRecipe.id}, Amount: ${site.targetAmountPerMinute}, nbMachine: ${site.numberOfMachines}")
        }
        return req
    }
*/
}