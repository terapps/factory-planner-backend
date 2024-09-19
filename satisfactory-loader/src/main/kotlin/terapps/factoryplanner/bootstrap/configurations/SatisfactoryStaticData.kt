package terapps.factoryplanner.bootstrap.configurations

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.GameObjectCategory
import terapps.factoryplanner.bootstrap.dto.SatisfactoryStaticData
import terapps.factoryplanner.bootstrap.dto.fromCategory
import terapps.factoryplanner.bootstrap.dto.generated.FGRecipe
import terapps.factoryplanner.bootstrap.dto.generated.FGSchematic
import terapps.factoryplanner.filestorage.dto.BucketEnum
import terapps.factoryplanner.filestorage.services.FileStorageService
import java.net.URL

@Component
class SatisfactoryStaticDataLoader {
    @Autowired
    private lateinit var fileStorageService: FileStorageService

    @Autowired
    @Qualifier("SatisfactoryDataMapper")
    private lateinit var objectMapper: ObjectMapper

    @Bean
    fun jsonData(): SatisfactoryStaticData {
        val categoryWeight = mapOf(
                FGRecipe::class to Int.MAX_VALUE - 1,
                FGSchematic::class to Int.MAX_VALUE,
        )
        val gameCategories = parseSatisfactoryJson()
        val recipeCategory = gameCategories.fromCategory(FGRecipe::class)

        recipeCategory.Classes += FGRecipe(
                "Recipe_Water_C",
                "Water extraction recipe",
                "Water extraction recipe",
                "",
                0.0,
                0.0,
                0.0,
                "(SelfMade.Build_WaterPump_C)",
                "((ItemClass=BlueprintGeneratedClass'/Game/FactoryGame/Resource/Raw.Desc_Water_C',Amount=1))",
                "",
                0.0,
                0.0
        )

        return gameCategories.sortedBy {
            categoryWeight[it.classType] ?: Int.MIN_VALUE
        }
    }

    private fun parseSatisfactoryJson(): SatisfactoryStaticData {
        val link = fileStorageService.getSignedUrl(BucketEnum.Resources, "data.json") // TODO config?

        return objectMapper.readValue(
                URL(link),
                object : TypeReference<List<GameObjectCategory<Any>>>() {}
        )
    }
}