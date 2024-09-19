package terapps.factoryplanner.bootstrap

import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

inline fun <reified T : Any> Parameter(name: String) = T::class.primaryConstructor!!.parameters.find {
    it.name == name
} ?: throw Error("Param [${name}] does not exist in ${T::class}")

fun Any.toMap() = this::class.memberProperties.associate {
    it as KProperty1<Any, Any?>
    it.name to it.get(this)
}