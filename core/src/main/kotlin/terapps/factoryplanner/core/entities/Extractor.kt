package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import java.util.*

@Node
data class Extractor(
        override val className: String,
        val displayName: String,
        val description: String,
        val extractCycleTime: Float,
        val itemsPerCycle: Int,
        val powerConsumption: Float,
        val powerConsumptionExponent: Float,
        override val minPotential: Float,
        override val maxPotential: Float,
        override val productionBoost: Float,
        val extractorType: String, // TODO types enum?
        val matchByAllowedResources: Boolean,
        val allowedResourceForm: Set<String>,
        val allowedResources: Set<String> // i dont want this since its going to end up a relationship
): Automaton {
        @Id
        @GeneratedValue
        lateinit var id: UUID
}

// TODO input/output info is empty in file, see in later version


