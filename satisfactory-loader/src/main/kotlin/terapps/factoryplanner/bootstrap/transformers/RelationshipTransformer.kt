package terapps.factoryplanner.bootstrap.transformers

import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

abstract class SatisfactoryRelationshipTransformer<Input : Any, Output : Any>(
        private val inputClass: KClass<Input>,
        relationship: Pair<KClass<*>, KClass<*>>,
        relationshipName: String, // TODO with props or something
): Transformer<Input, Output>,  RelationshipQuery(relationship, relationshipName) {
    override fun supportsClass(clazz: KClass<*>): Boolean {
        return inputClass.isSubclassOf(clazz)
    }
}