package terapps.factoryplanner

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import terapps.factoryplanner.bootstrap.SatisfactoryEntitiesLoader

@SpringBootApplication

@EnableNeo4jRepositories(basePackages = ["terapps.factoryplanner.core.entities"])
class FactoryLoaderApplication: CommandLineRunner {
	@Autowired
	lateinit var satisfactoryEntitiesLoader: SatisfactoryEntitiesLoader

	override fun run(vararg args: String?) {
		satisfactoryEntitiesLoader.init()
	}

}


fun main(args: Array<String>) {
	runApplication<FactoryLoaderApplication>(*args)
}