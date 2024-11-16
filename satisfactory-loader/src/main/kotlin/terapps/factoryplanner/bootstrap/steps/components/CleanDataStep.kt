package terapps.factoryplanner.bootstrap.steps.components

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.configurations.ReloadProfile
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
    private lateinit var schematicDependencyRepository: SchematicDependencyRepository

    @Autowired
    private lateinit var reloadProfile: ReloadProfile

    private val logger = LoggerFactory.getLogger(this::class.java)
    override val priority: Int
        get() = 0

    override fun prepare() {
        if (reloadProfile.wipe) {
            logger.info("Wiping data enabled")
            itemDescriptorRepository.deleteAll()
            craftingMachineRepository.deleteAll()
            extractorRepository.deleteAll()
            recipeRepository.deleteAll()
            schematicRepository.deleteAll()
            schematicDependencyRepository.deleteAll()
        }
    }

    override fun dispose() {

    }
}