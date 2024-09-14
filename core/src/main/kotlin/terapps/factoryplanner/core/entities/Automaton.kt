package terapps.factoryplanner.core.entities

interface Automaton {
    val className: String
    val minPotential: Float
    val maxPotential: Float
    val productionBoost: Float
}