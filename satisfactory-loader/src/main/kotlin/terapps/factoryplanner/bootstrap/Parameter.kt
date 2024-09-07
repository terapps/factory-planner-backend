package terapps.factoryplanner.bootstrap

import kotlin.reflect.full.primaryConstructor

inline fun <reified T : Any> Parameter(name: String) = T::class.primaryConstructor!!.parameters.find {
    it.name == name
} ?: throw Error("Param [${name}] does not exist in ${T::class}")