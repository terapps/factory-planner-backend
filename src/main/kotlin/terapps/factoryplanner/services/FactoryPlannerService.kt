package terapps.factoryplanner.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import terapps.factoryplanner.entities.satisfactory.ItemDescriptorRepository
import terapps.factoryplanner.entities.satisfactory.RecipeRepository
import terapps.factoryplanner.services.components.FactoryRequirementBuilder
import terapps.factoryplanner.services.components.FactorySite

@Service
class FactoryPlannerService {
    @Autowired
    lateinit var recipeRepository: RecipeRepository

    @Autowired
    lateinit var itemDescriptorRepository: ItemDescriptorRepository

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
}