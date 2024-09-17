package terapps.factoryplanner.bootstrap.steps

interface RootStep {
    val priority: Int // TODO runAfter would be more readable

    fun prepare()
    fun dispose()
}