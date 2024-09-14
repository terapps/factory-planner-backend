package terapps.factoryplanner.bootstrap.steps

import terapps.factoryplanner.bootstrap.dto.GameObjectCategory
import kotlin.reflect.KClass

abstract class Step<StepClass: Any>(val step: KClass<StepClass>) {
    abstract fun prepare(category: GameObjectCategory<StepClass>)
    abstract fun dispose(category: GameObjectCategory<StepClass>)
}

interface RootStep {
    fun prepare() {}
    fun dispose() {}
}