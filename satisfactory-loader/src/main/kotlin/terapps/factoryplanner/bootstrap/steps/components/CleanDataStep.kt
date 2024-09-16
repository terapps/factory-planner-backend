package terapps.factoryplanner.bootstrap.steps.components

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.configuration.ReloadProfile
import terapps.factoryplanner.bootstrap.steps.RootStep
import terapps.factoryplanner.core.repositories.*

@Component
class CleanDataStep : RootStep {

    @Autowired
    private lateinit var extractorRepository: ExtractorRepository

    @Autowired
    private lateinit var craftingMachineRepository: CraftingMachineRepository

    @Autowired
    private lateinit var itemDescriptorRepository: ItemDescriptorRepository

    @Autowired
    private lateinit var recipeRepository: RecipeRepository
    @Autowired
    private lateinit var schematicRepository: SchematicRepository

    @Autowired
    private lateinit var reloadProfile: ReloadProfile

    override fun prepare() {
        if (reloadProfile.wipe) {
            println("Cleaning old data...")
            itemDescriptorRepository.deleteAll()
            craftingMachineRepository.deleteAll()
            extractorRepository.deleteAll()
            recipeRepository.deleteAll()
            schematicRepository.deleteAll()
        }
    }

    override fun dispose() {

    }
}