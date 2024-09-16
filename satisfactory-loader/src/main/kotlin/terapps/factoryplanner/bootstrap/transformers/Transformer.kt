package terapps.factoryplanner.bootstrap.transformers

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

interface Transformer<in Input : Any, Output : Any> {
    fun transform(transformIn: Input): Output

    fun supportsClass(clazz: KClass<*>): Boolean
}

