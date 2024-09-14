package terapps.factoryplanner

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import terapps.factoryplanner.core.entities.ItemCategory
import terapps.factoryplanner.core.entities.RecipeRepository
import terapps.factoryplanner.core.services.RecipeWeightService

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableNeo4jRepositories(basePackages = ["terapps.factoryplanner.core"])
class FactoryLoaderApplication : CommandLineRunner {

    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    @Autowired
    private lateinit var recipeWeightService: RecipeWeightService

    override fun run(vararg args: String?) {
/*  TODO that will be through http and inputs a factory site instead of a recipe.
      println("Fetching recipe for weight computing...")
        val recipes = recipeRepository.findByProducingItemCategoryIn(listOf(ItemCategory.Craftable, ItemCategory.Raw))
        val buggy = listOf("Recipe_UnpackageNitricAcid_C","Recipe_PackagedCrudeOil_C","Recipe_UnpackageAlumina_C","Recipe_UnpackageBioFuel_C", "Recipe_UnpackageTurboFuel_C", "Recipe_PackagedWater_C", "Recipe_Alternate_DilutedPackagedFuel_C", "Recipe_PackagedTurboFuel_C", "Recipe_Alternate_HeatFusedFrame_C")

        recipes.forEach {
            println("Compute weight for ${it.getClassName()}")
            if (!buggy.contains(it.getClassName())) {
                try {
                    recipeWeightService.computeRecipeWeight(it)
                } catch (error: Error) {
                    println("Skipped ${it.getClassName()}: ${error.message}")
                }
            }
        }*/
    }

}

fun main(args: Array<String>) {
    runApplication<FactoryLoaderApplication>(*args)
}