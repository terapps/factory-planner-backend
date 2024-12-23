package terapps.factoryplanner.core.repositories

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository
import terapps.factoryplanner.core.entities.TaskEntity
import terapps.factoryplanner.core.entities.TaskGroupEntity
import terapps.factoryplanner.core.entities.TaskIoEntity

@Repository
interface TaskRepository : Neo4jRepository<TaskEntity, String> {
    fun <T> findById(id: String, clazz: Class<T>): T?
}

@Repository
interface TaskIORepository : Neo4jRepository<TaskIoEntity, String> {
}

@Repository
interface TaskGroupRepository : Neo4jRepository<TaskGroupEntity, String> {
    fun <T> findById(id: String, clazz: Class<T>): T?
    fun findByNameLikeIgnoreCase(name: String): Collection<TaskGroupEntity>
}