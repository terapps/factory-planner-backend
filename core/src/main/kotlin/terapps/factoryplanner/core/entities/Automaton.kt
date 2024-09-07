package terapps.factoryplanner.core.entities

interface Automaton {
    val id: String
    val minPotential: Float
    val maxPotential: Float
    val maxPotentialIncreasePerCrystal: Float
}