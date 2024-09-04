package terapps.factoryplanner.bootstrap

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import terapps.factoryplanner.core.entities.ItemDescriptorRepository
import terapps.factoryplanner.core.entities.Recipe
import terapps.factoryplanner.core.entities.RecipeRepository
import terapps.factoryplanner.core.services.components.FactoryRequirementBuilder

@Service
class RecipeWeightService {
    @Autowired
    lateinit var recipeRepository: RecipeRepository


    @Autowired
    lateinit var itemDescriptorRepository: ItemDescriptorRepository

    fun compute(recipe: Recipe) {
        println("computing ${recipe.id}")
        val sites = FactoryRequirementBuilder.newBuilder(recipeRepository, itemDescriptorRepository).apply {
            recipe.produces.forEach {
                try {
                    addRequirement(it.descriptor, recipe, it.outputPerCycle)
                } catch (e: Error) {
                    println(e)
                }
            }
        }.build()
        recipe.weightedPoints = sites.sumOf { (it.weight ?: 0f).toDouble() }.toFloat()
        /* TODO
                    recipe.energyPoints = sites.sumOf {  }.toFloat()
        */
        recipe.buildingCountPoints = sites.sumOf {
            (it.numberOfMachines ?: it.nbMaxedExtractors)!!.toDouble()
        }.toFloat()
        recipe.sinkingPoints = sites.filter { it.targetDescriptor.sinkablePoints != null }.sumOf { (it.targetDescriptor.sinkablePoints!!.toFloat() * it.targetAmountPerMinute).toDouble() }.toFloat()
    }
}