package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import java.util.UUID

@Node("CraftingMachine")
class CraftingMachineEntity(
        @Id
        override val className: String,
        override val displayName: String,
        val description: String,
        val manufacturingSpeed: Double = 0.0,
        val powerConsumption: Double = 0.0,
        val powerConsumptionExponent: Double = 0.0,
        override val minPotential: Double = 0.0,
        override val maxPotential: Double = 0.0,
        override val productionBoost: Double = 0.0,
        @Relationship(type = "IS_REPRESENTED_BY", direction = Relationship.Direction.OUTGOING)
        val descriptor: ItemDescriptorEntity? = null,
): Automaton {
}
// TODO input/output info is empty in file, see in later version


