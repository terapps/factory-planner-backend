package terapps.factoryplanner.entities.satisfactory

import org.neo4j.ogm.annotation.GeneratedValue
import org.neo4j.ogm.annotation.NodeEntity
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@NodeEntity
data class ItemDescriptor(
        @Id
        @GeneratedValue
        val id: String,
        val displayName: String,
        val description: String,
        val form: String? = null,
        val isRaw: Boolean = false,
        val isBuildable: Boolean = false,
)

@Repository
interface ItemDescriptorRepository : Neo4jRepository<ItemDescriptor, String> {
    fun findAllByFormInAndIsRawIsTrue(form: List<String>): List<ItemDescriptor>
}
