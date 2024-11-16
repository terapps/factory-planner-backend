package terapps.factoryplanner.bootstrap.transformers

import org.springframework.data.neo4j.core.schema.Node
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubclassOf

abstract class SatisfactoryRelationshipTransformer<Input : Any, Output : Any>(
        private val inputClass: KClass<Input>,
        relationship: Pair<KClass<*>, KClass<*>>,
        relationshipName: String, // TODO with props or something
): Transformer<Input, Output>,  RelationshipQuery(Pair(getEntityName(relationship.first), getEntityName(relationship.second)), relationshipName) {
    companion object {
        fun getEntityName(kClass: KClass<*>): String {
            val annotation = kClass.findAnnotation<Node>() ?: throw Error("${kClass.simpleName} is not a Node")

            return annotation.value.firstOrNull() ?:  kClass.simpleName!!
        }
    }
    override fun supportsClass(clazz: KClass<*>): Boolean {
        return inputClass.isSubclassOf(clazz)
    }
}