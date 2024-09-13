package terapps.factoryplanner.bootstrap.dto


import kotlin.reflect.KClass


data class GameObjectCategory<T: Any>(
        val NativeClass: String,
        val classType: KClass<T>,
        var Classes: MutableList<T>
)