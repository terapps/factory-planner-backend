package terapps.factoryplanner.bootstrap.transformers

import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor


class BatchList<T>(
        private val bulkSize: Int = 300,
        private val onLimit: (batch: MutableCollection<T>) -> Unit,
) {
    private val internal: MutableCollection<T> = mutableListOf()

     fun add(element: T) {
         val res = internal.add(element)
         if (!res) {
             throw Error("Error saving bulk")
         }
         if (internal.size > bulkSize) {
             onBatch()
         }
     }

    fun onBatch() {
        onLimit(internal)
        internal.clear()
    }
}

interface EntityTransformer<Input : Any, Output : Any>: Transformer<Input, Output> {
    val batch: BatchList<Output>

    fun save(output: Output): Output = batch.let {
        it.add(output)
        output
    }
}

abstract class AbstractTransformer<Input : Any, Output : Any>(
        private val inputClass: KClass<Input>,
) : EntityTransformer<Input, Output> {
    override fun supportsClass(clazz: KClass<*>): Boolean {
        return inputClass.isSubclassOf(clazz)
    }
}

abstract class GenericAbstractTransformer<Input : Any, Output : Any>(
        private val outputClass: KClass<Output>,
        private val supportedFgClasses: Collection<KClass<*>>
) : EntityTransformer<Input, Output> {
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