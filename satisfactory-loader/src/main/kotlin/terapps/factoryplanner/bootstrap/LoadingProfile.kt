package terapps.factoryplanner.bootstrap

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import terapps.factoryplanner.core.entities.CraftingMachineRepository
import terapps.factoryplanner.core.entities.ExtractorRepository
import terapps.factoryplanner.core.entities.ItemDescriptorRepository
import terapps.factoryplanner.core.entities.RecipeRepository

@Component
class LoadingProfile {
    @Value("\${satisfactory.planner.data.force-reload}")
    var forceReload: Boolean? = null

    @Autowired
    private lateinit var itemDescriptorRepository: ItemDescriptorRepository

    @Autowired
    private lateinit var craftingMachineRepository: CraftingMachineRepository

    @Autowired
    private lateinit var extractorRepository: ExtractorRepository

    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    @Autowired
    private lateinit var satisfactoryEntitiesLoader: SatisfactoryEntitiesLoader

    @PostConstruct
    fun init() {
        if (forceReload == true) {
            println("Deleting old data")

            itemDescriptorRepository.deleteAll()
            craftingMachineRepository.deleteAll()
            recipeRepository.deleteAll()
            extractorRepository.deleteAll()

            println("Deleting loading new data")

            satisfactoryEntitiesLoader.init()
        }
    }
}