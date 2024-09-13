package terapps.factoryplanner.bootstrap.steps.components

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.configuration.ReloadProfile
import terapps.factoryplanner.core.entities.*

@Component
class CleanDataStep : RootStep {
    @Autowired
    private lateinit var extractorRepository: ExtractorRepository

    @Autowired
    private lateinit var craftingMachineRepository: CraftingMachineRepository
    @Autowired
    private lateinit var reloadProfile: ReloadProfile

    @Autowired
    private lateinit var itemDescriptorRepository: ItemDescriptorRepository

    override fun prepare() {
        if (reloadProfile.wipe) {
            println("Cleaning old data...")
            itemDescriptorRepository.deleteAll()
            craftingMachineRepository.deleteAll()
            extractorRepository.deleteAll()
        }
    }
}