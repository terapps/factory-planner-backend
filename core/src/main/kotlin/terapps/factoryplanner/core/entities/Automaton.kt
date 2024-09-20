package terapps.factoryplanner.core.entities

interface Automaton {
    val displayName: String
    val className: String
    val minPotential: Double
    val maxPotential: Double
    val productionBoost: Double
}