package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import java.util.*

@Node("Extractor")
class ExtractorEntity(
        @Id
        override val className: String,
        override val displayName: String,
        val description: String,
        val extractCycleTime: Double,
        val itemsPerCycle: Int,
        val powerConsumption: Double,
        val powerConsumptionExponent: Double,
        override val minPotential: Double,
        override val maxPotential: Double,
        override val productionBoost: Double,
        val extractorType: String, // TODO types enum?
        val matchByAllowedResources: Boolean,
        val allowedResourceForm: Set<String>,
        val allowedResources: Set<String>, // i dont want this since its going to end up a relationship
        @Relationship(type = "IS_REPRESENTED_BY", direction = Relationship.Direction.OUTGOING)
        val descriptor: ItemDescriptorEntity? = null, // TODo summary type
): Automaton {
}

// TODO input/output info is empty in file, see in later version


