package terapps.factoryplanner.bootstrap

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
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
    private lateinit var objectMapper: ObjectMapper


    @Autowired
    private lateinit var stepManager: StepManager

    @Autowired
    private lateinit var satisfactoryDataTransformer: SatisfactoryDataTransformer

    private fun parseSatisfactoryJson(): List<GameObjectCategory> {
        return objectMapper.readValue(
                resourceFile.file,
                object : TypeReference<List<GameObjectCategory>>() {}
        )
    }

    fun init() {
        // TODO chunk parsing into smaller files
        val gameCategories = parseSatisfactoryJson().sortedBy {
            it.classType.isSubclassOf(FGRecipe::class)
        }

        gameCategories.forEachIndexed { index, it ->
            println("Loading game category ${index} / ${gameCategories.size}")

            stepManager.prepareStep(it.classType)
            it.Classes.forEach {
                satisfactoryDataTransformer.transform(it)
            }
            stepManager.disposeStep(it.classType)
        }
    }
}