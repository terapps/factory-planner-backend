package terapps.factoryplanner.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories


@SpringBootApplication(scanBasePackages = ["terapps.factoryplanner"])
@ConfigurationPropertiesScan(basePackages = ["terapps.factoryplanner"])
@EnableNeo4jRepositories(basePackages = ["terapps.factoryplanner"])
class FactoryPlannerApplication

fun main(args: Array<String>) {
    runApplication<FactoryPlannerApplication>(*args)
}

