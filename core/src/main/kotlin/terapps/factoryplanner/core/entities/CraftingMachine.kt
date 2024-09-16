package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import java.util.UUID

@Node
data class CraftingMachine(
        override val className: String,
        val displayName: String,
        val description: String,
        val manufacturingSpeed: Double = 0.0,
        val powerConsumption: Double = 0.0,
        val powerConsumptionExponent: Double = 0.0,
        override val minPotential: Double = 0.0,
        override val maxPotential: Double = 0.0,
        override val productionBoost: Double = 0.0,
        val manual: Boolean = false,
): Automaton {
        @Id
        @GeneratedValue
        lateinit var id: UUID

}
// TODO input/output info is empty in file, see in later version


