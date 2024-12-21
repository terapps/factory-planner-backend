package terapps.factoryplanner.core.repositories

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository
import terapps.factoryplanner.core.entities.PowerGeneratorEntity
import terapps.factoryplanner.core.entities.TaskEntity
import terapps.factoryplanner.core.entities.TaskGroupEntity
import java.util.UUID

@Repository
interface TaskRepository : Neo4jRepository<TaskEntity, String> {
}

@Repository
interface TaskGroupRepository : Neo4jRepository<TaskGroupEntity, String> {
    fun findByNameLikeIgnoreCase(name: String): Collection<TaskGroupEntity>
}