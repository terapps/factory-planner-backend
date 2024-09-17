package terapps.factoryplanner.core.repositories

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository
import terapps.factoryplanner.core.entities.ItemCategory
import terapps.factoryplanner.core.entities.ItemDescriptor
import java.util.*

@Repository
interface ItemDescriptorRepository : Neo4jRepository<ItemDescriptor, UUID> {
    fun <T> findByClassName(className: String, clazz: Class<T>): T?
}