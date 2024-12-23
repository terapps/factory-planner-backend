package terapps.factoryplanner.core.repositories

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository
import terapps.factoryplanner.core.entities.ItemCategory
import terapps.factoryplanner.core.entities.ItemDescriptorEntity
import java.util.*

@Repository
interface ItemDescriptorRepository : Neo4jRepository<ItemDescriptorEntity, String> {
    fun existsByClassName(id: String): Boolean
    fun <T> findByCategory(category: ItemCategory, clazz: Class<T>): T?
    fun <T> findByClassName(className: String, clazz: Class<T>): T?
    fun <T> findByDisplayNameLikeIgnoreCase(displayName: String, clazz: Class<T>): Collection<T>
}