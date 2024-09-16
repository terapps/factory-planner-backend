package terapps.factoryplanner

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import terapps.factoryplanner.bootstrap.steps.StepManager
import terapps.factoryplanner.core.entities.ItemCategory
import terapps.factoryplanner.core.entities.RecipeRepository
import terapps.factoryplanner.core.services.RecipeWeightService

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableNeo4jRepositories(basePackages = ["terapps.factoryplanner.core"])
class FactoryLoaderApplication : CommandLineRunner {
    @Autowired
    private lateinit var stepManager: StepManager

    override fun run(vararg args: String?) {
        stepManager.prepare()
        stepManager.dispose()
    }

}

fun main(args: Array<String>) {
    runApplication<FactoryLoaderApplication>(*args)
}