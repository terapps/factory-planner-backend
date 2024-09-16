package terapps.factoryplanner.core.entities

interface Automaton {
    val className: String
    val minPotential: Double
    val maxPotential: Double
    val productionBoost: Double
}