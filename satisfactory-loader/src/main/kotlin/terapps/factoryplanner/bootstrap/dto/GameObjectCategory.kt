package terapps.factoryplanner.bootstrap.dto


import kotlin.reflect.KClass


data class GameObjectCategory(
        val NativeClass: String,
        val classType: KClass<*>,
        val Classes: List<GameEntity>
)