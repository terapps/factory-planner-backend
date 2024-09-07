package terapps.factoryplanner.bootstrap.steps.components

interface Step {
    fun prepare() {}
    fun dispose() {}
}
