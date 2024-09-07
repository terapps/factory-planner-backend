package terapps.factoryplanner.bootstrap.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import kotlin.reflect.full.findAnnotation

class GameCategoryDeserializer(val om: ObjectMapper) : JsonDeserializer<GameObjectCategory>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): GameObjectCategory {
        val node: JsonNode = p.codec.readTree(p)
        val nativeClass = node.get("NativeClass").asText()
        val classesNode = node.get("Classes")
        val jsonSubTypes = GameEntity::class.findAnnotation<JsonSubTypes>()
                ?: throw IllegalArgumentException("No JsonSubTypes annotation found on GameEntity")
        val actualType = jsonSubTypes.value.first {
            it.name == nativeClass
        }.value

        val gameEntities: List<GameEntity> = classesNode.map {
            val res = om.convertValue(it, actualType.java) as GameEntity
            res
        }
        return GameObjectCategory(NativeClass = nativeClass, classType = actualType,Classes = gameEntities)

    }
}

@Component
class JsonParsing {
    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper().apply {
        val satiCategoryModule = SimpleModule().addDeserializer(GameObjectCategory::class.java, GameCategoryDeserializer(this))

        registerModules(KotlinModule(), satiCategoryModule)

        enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        enable(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION)
    }
}