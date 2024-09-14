package terapps.factoryplanner.bootstrap.transformers

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

interface SatisfactoryTransformer<in Input : Any, Output : Any> {
    fun transform(transformIn: Input): Output

    fun supportsClass(clazz: KClass<*>): Boolean
    fun save(output: Output): Output {
        return output
    }
}

abstract class AbstractTransformer<Input : Any, Output : Any>(
        private val inputClass: KClass<Input>,
) : SatisfactoryTransformer<Input, Output> {
    override fun supportsClass(clazz: KClass<*>): Boolean {
        return inputClass.isSubclassOf(clazz)
    }

}

abstract class GenericAbstractTransformer<Input : Any, Output : Any>(
        private val outputClass: KClass<Output>,
        private val supportedFgClasses: Collection<KClass<*>>
) : SatisfactoryTransformer<Input, Output> {
    override fun supportsClass(clazz: KClass<*>): Boolean {
        return supportedFgClasses.contains(clazz)
    }

    override fun transform(transformIn: Input): Output {
        try {
            val genericInput = transformIn.generifyConstructorParams()
            val constructorParams = genericInput.makeConstructorParams(transformIn)
            val ctor = outputClass.primaryConstructor!!

            return ctor.callBy(constructorParams)
        } catch (e: Exception) {
            throw Error("Couldnt autotransform ${this::class}", e)
        }
    }

    protected fun Any.generifyConstructorParams(): Map<*, *> = this::class.memberProperties.associate { property ->
        property.name to property.getter.call(this)
    }

    protected abstract fun Map<*, *>.makeConstructorParams(orig: Any): Map<KParameter, Any?>


}