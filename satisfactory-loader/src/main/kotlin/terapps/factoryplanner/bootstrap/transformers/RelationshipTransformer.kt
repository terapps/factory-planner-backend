package terapps.factoryplanner.bootstrap.transformers

import terapps.factoryplanner.core.services.components.RelationshipQuery
import terapps.factoryplanner.core.services.components.getEntityName
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

abstract class SatisfactoryRelationshipTransformer<Input : Any, Output : Any>(
        private val inputClass: KClass<Input>,
        relationship: Pair<KClass<*>, KClass<*>>,
        relationshipName: String, // TODO with props or something
): Transformer<Input, Output>,  RelationshipQuery(Pair(getEntityName(relationship.first), getEntityName(relationship.second)), relationshipName) {

    override fun supportsClass(clazz: KClass<*>): Boolean {
        return inputClass.isSubclassOf(clazz)
    }
}