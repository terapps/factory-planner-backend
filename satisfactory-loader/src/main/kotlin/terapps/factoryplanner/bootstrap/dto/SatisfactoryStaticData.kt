package terapps.factoryplanner.bootstrap.dto

import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

typealias SatisfactoryStaticData = List<GameObjectCategory<*>>
fun <T: GameEntity>SatisfactoryStaticData.fromCategory(kClass: KClass<T>): GameObjectCategory<T> {
    return find { it.classType.isSubclassOf(kClass) } as GameObjectCategory<T>
}