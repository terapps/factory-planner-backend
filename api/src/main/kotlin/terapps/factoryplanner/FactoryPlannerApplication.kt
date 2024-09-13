package terapps.factoryplanner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableNeo4jRepositories(basePackages = ["terapps.factoryplanner.core"])
class FactoryPlannerApplication

fun main(args: Array<String>) {
	runApplication<FactoryPlannerApplication>(*args)
}

