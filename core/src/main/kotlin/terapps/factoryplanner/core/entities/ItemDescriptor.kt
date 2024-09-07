package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

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
        @Id
        val id: String,
        val displayName: String,
        val description: String,
        val category: ItemCategory,
        val sinkablePoints: Int? = null,
        val energyValue: Float? = null,
        val form: String? = null,
        @Relationship(type = "PRODUCED_BY", direction = Relationship.Direction.INCOMING)
        var producedBy: Set<ItemDescriptorProducedBy> = emptySet()
) {

}

@Repository
interface ItemDescriptorRepository : Neo4jRepository<ItemDescriptor, String> {
    fun findByIdIn(ids: List<String>): Collection<ItemDescriptor>

    fun findAllByFormInAndCategory(form: List<String>, category: ItemCategory): List<ItemDescriptor>
}

