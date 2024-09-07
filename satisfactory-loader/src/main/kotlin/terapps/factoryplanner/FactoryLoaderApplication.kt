package terapps.factoryplanner

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories
import terapps.factoryplanner.bootstrap.SatisfactoryEntitiesLoader
import terapps.factoryplanner.core.entities.ItemDescriptorRepository

@SpringBootApplication

@EnableNeo4jRepositories(basePackages = ["terapps.factoryplanner.core",])
class FactoryLoaderApplication: CommandLineRunner {
	@Autowired
	private lateinit var itemDescriptorRepository: ItemDescriptorRepository

	override fun run(vararg args: String?) {
		println("Finished running :)")

		val truc = itemDescriptorRepository.findById("Desc_Plastic_C").orElseThrow()

		println(truc)
	}

}


fun main(args: Array<String>) {
	runApplication<FactoryLoaderApplication>(*args)
}