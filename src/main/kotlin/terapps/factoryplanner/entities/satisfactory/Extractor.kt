package terapps.factoryplanner.entities.satisfactory

import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.NodeEntity
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Relationship
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@NodeEntity
data class Extractor(
        @Id
        @GeneratedValue
        val id: String,
        val displayName: String,
        val description: String,
        val extractCycleTime: Float,
        val itemsPerCycle: Float,
        val powerConsumption: Float,
        val powerConsumptionExponent: Float,
        val minPotential: Float,
        val maxPotential: Float,
        val maxPotentialIncreasePerCrystal: Float,
        val extractorType: String, // TODO types enum?
        @Relationship(type = "ALLOWED_RESOURCES", direction = Relationship.Direction.INCOMING)
        val allowedResources: Set<ItemDescriptor>
)

// TODO input/output info is empty in file, see in later version


@Repository
interface ExtractorRepository : Neo4jRepository<Extractor, String> {
}
