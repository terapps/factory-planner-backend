package terapps.factoryplanner.core.repositories

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository
import terapps.factoryplanner.core.entities.ItemCategory
import terapps.factoryplanner.core.entities.ItemDescriptor
import java.util.*

@Repository
interface ItemDescriptorRepository : Neo4jRepository<ItemDescriptor, UUID> {
    fun findAllByCategory(category: ItemCategory): Collection<ItemDescriptor>

    fun findAllByForm(form: String): Collection<ItemDescriptor>

    fun findAllByFormInAndCategory(form: List<String>, category: ItemCategory): List<ItemDescriptor>
}