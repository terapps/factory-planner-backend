package terapps.factoryplanner.bootstrap.configuration

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.GameCategoryDeserializer
import terapps.factoryplanner.bootstrap.dto.GameObjectCategory
import terapps.factoryplanner.bootstrap.dto.SatisfactoryStaticData
import terapps.factoryplanner.bootstrap.dto.generated.FGRecipe
import terapps.factoryplanner.bootstrap.dto.generated.FGSchematic

@Component
class SatisfactoryJsonParser {
    @Bean
    @Qualifier("SatisfactoryDataMapper")
    fun objectMapper(): ObjectMapper = ObjectMapper().apply {
        val satiCategoryModule = SimpleModule().addDeserializer(GameObjectCategory::class.java, GameCategoryDeserializer(this))

        registerModules(KotlinModule(), satiCategoryModule)

        enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        enable(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION)
    }
}