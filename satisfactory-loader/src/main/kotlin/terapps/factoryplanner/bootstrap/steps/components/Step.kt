package terapps.factoryplanner.bootstrap.steps.components

import terapps.factoryplanner.bootstrap.dto.GameObjectCategory

interface Step {
    fun prepare(category: GameObjectCategory<Any>) {}
    fun dispose(category: GameObjectCategory<Any>) {}
}

interface RootStep {
    fun prepare() {}
    fun dispose() {}
}