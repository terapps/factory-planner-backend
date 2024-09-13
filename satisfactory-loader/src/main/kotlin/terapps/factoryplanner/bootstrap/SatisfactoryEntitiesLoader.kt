package terapps.factoryplanner.bootstrap

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import terapps.factoryplanner.bootstrap.configuration.ReloadProfile
import terapps.factoryplanner.bootstrap.dto.GameObjectCategory
import terapps.factoryplanner.bootstrap.dto.generated.FGRecipe
import terapps.factoryplanner.bootstrap.steps.StepManager
import terapps.factoryplanner.bootstrap.transformers.SatisfactoryDataTransformer
import kotlin.reflect.full.isSubclassOf

@Service
class SatisfactoryEntitiesLoader {
    @Value("classpath:data.json")
    private lateinit var resourceFile: Resource

    @Autowired
    private lateinit var reloadProfile: ReloadProfile

    @Autowired
    private lateinit var objectMapper: ObjectMapper


    @Autowired
    private lateinit var stepManager: StepManager

    @Autowired
    private lateinit var satisfactoryDataTransformer: SatisfactoryDataTransformer

    private fun parseSatisfactoryJson(): List<GameObjectCategory<Any>> {
        return objectMapper.readValue(
                resourceFile.file,
                object : TypeReference<List<GameObjectCategory<Any>>>() {}
        )
    }

    @PostConstruct
    fun init() {
        if (!reloadProfile.wipe && reloadProfile.steps.isEmpty()) {
            return
        }
        // TODO chunk parsing into smaller files
        val gameCategories = parseSatisfactoryJson().sortedBy {
            it.classType.isSubclassOf(FGRecipe::class)
        }

        stepManager.prepare()
        gameCategories.forEachIndexed { index, gameCategory ->

            if (reloadProfile.wipe || reloadProfile.steps.any { gameCategory.NativeClass.contains(it) }) {

                satisfactoryDataTransformer.transformCategory(gameCategory) {
                    println("Loading game category ${index + 1} / ${gameCategories.size}")
                }
            }
        }
        stepManager.dispose()
    }
}