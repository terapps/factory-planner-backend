package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@Node
data class Extractor(
        @Id
        override val id: String,
        val displayName: String,
        val description: String,
        val extractCycleTime: Float,
        val itemsPerCycle: Int,
        val powerConsumption: Float,
        val powerConsumptionExponent: Float,
        override val minPotential: Float,
        override val maxPotential: Float,
        override val maxPotentialIncreasePerCrystal: Float,
        val extractorType: String, // TODO types enum?
        val allowedResources: Set<String>
): Automaton

// TODO input/output info is empty in file, see in later version


@Repository
interface ExtractorRepository : Neo4jRepository<Extractor, String> {
}
