package terapps.factoryplanner.bootstrap.configurations

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.GameCategoryDeserializer
import terapps.factoryplanner.bootstrap.dto.GameObjectCategory

@Component
class SatisfactoryJsonParser {
    @Bean
    @Qualifier("SatisfactoryDataMapper")
    fun objectMapper(): ObjectMapper = ObjectMapper().apply {
        val satiCategoryModule = SimpleModule().addDeserializer(GameObjectCategory::class.java, GameCategoryDeserializer(this))

        registerModules(KotlinModule.Builder().build(), satiCategoryModule)

        enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        enable(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION)
    }
}