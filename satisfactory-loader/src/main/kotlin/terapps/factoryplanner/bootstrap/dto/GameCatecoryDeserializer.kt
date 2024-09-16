package terapps.factoryplanner.bootstrap.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

class GameCategoryDeserializer(val om: ObjectMapper) : JsonDeserializer<GameObjectCategory<Any>>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): GameObjectCategory<Any> {
        val node: JsonNode = p.codec.readTree(p)
        val nativeClass = node.get("NativeClass").asText()
        val classesNode = node.get("Classes")
        val jsonSubTypes = GameEntity::class.findAnnotation<JsonSubTypes>()
                ?: throw IllegalArgumentException("No JsonSubTypes annotation found on GameEntity")
        val actualType = jsonSubTypes.value.firstOrNull {
            it.name == nativeClass
        }?.value ?: throw Error("Cannot map type ${nativeClass}")

        val gameEntities: List<GameEntity> = classesNode.map {
            val res = om.convertValue(it, actualType.java) as GameEntity
            res
        }
        return GameObjectCategory(NativeClass = nativeClass, classType = actualType as KClass<Any>,Classes = gameEntities.toMutableList())

    }
}
