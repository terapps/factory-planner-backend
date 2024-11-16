package terapps.factoryplanner.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories

@SpringBootApplication(scanBasePackages = ["terapps.factoryplanner.core"])
@ConfigurationPropertiesScan(basePackages = ["terapps.factoryplanner.core"])
@EnableNeo4jRepositories(basePackages = ["terapps.factoryplanner.core"])
class FactoryPlannerTestApplication

fun main(args: Array<String>) {
    runApplication<FactoryPlannerTestApplication>(*args)
}
