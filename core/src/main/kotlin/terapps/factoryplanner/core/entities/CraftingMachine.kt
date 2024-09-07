package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@Node
data class CraftingMachine(
        @Id
        override val id: String,
        val displayName: String,
        val description: String,
        val manufacturingSpeed: Float = 0f,
        val powerConsumption: Float = 0f,
        val powerConsumptionExponent: Float = 0f,
        override val minPotential: Float = 0f,
        override val maxPotential: Float = 0f,
        override val maxPotentialIncreasePerCrystal: Float = 0f,
        val manual: Boolean = false,
): Automaton
// TODO input/output info is empty in file, see in later version


@Repository
interface CraftingMachineRepository : Neo4jRepository<CraftingMachine, String> {
}
