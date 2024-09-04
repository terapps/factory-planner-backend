package terapps.factoryplanner.core.entities

import org.neo4j.ogm.annotation.NodeEntity
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository
enum class ItemCategory {
    Raw,
    Biomass,
    Building,
    Equipment,
    Craftable,
    Vehcle,
    Consumable


}

@NodeEntity
data class ItemDescriptor(
        @Id
        val id: String,
        val displayName: String,
        val description: String,
        val category: ItemCategory,
        val sinkablePoints: Int? = null,
        val energyValue: Float? = null,
        val form: String? = null,
)

@Repository
interface ItemDescriptorRepository : Neo4jRepository<ItemDescriptor, String> {
    fun findAllByFormInAndCategory(form: List<String>, category: ItemCategory): List<ItemDescriptor>
}
