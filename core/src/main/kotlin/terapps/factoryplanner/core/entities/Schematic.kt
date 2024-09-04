package terapps.factoryplanner.core.entities
import org.neo4j.ogm.annotation.NodeEntity
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@NodeEntity
data class Schematic(
        @Id
        val id: String,
        val displayName: String,
        val description: String,
        val type: String,
        val cost: Set<ItemIO>
)

@Repository
interface SchematicRepository : Neo4jRepository<Schematic, String> {
}
