package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository
import java.util.*

enum class ItemCategory {
    Raw,
    Biomass,
    Building,
    Equipment,
    Craftable,
    Vehicle,
    Consumable
}

@Node
data class ItemDescriptor(
        val className: String?,
        val displayName: String?,
        val description: String?,
        val category: ItemCategory?,
        val sinkablePoints: Int? = null,
        val energyValue: Float? = null,
        val form: String? = null, // TODO enum
        @Relationship(type = "EXTRACTED_IN", direction = Relationship.Direction.OUTGOING)
        val extractedIn: Set<Extractor> = emptySet(),
        @Relationship(type = "PRODUCED_BY", direction = Relationship.Direction.INCOMING)
        var producedBy: Set<ItemDescriptorProducedBy> = emptySet()
) {
    @Id
    @GeneratedValue
    lateinit var id: UUID
}

@Repository
interface ItemDescriptorRepository : Neo4jRepository<ItemDescriptor, UUID> {
    fun findAllByCategory(category: ItemCategory): Collection<ItemDescriptor>

    fun findAllByForm(form: String): Collection<ItemDescriptor>

    fun findAllByFormInAndCategory(form: List<String>, category: ItemCategory): List<ItemDescriptor>
}

