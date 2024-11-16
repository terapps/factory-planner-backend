package terapps.factoryplanner.core.repositories

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository
import terapps.factoryplanner.core.entities.SchematicDependencyEntity

@Repository
interface SchematicDependencyRepository : Neo4jRepository<SchematicDependencyEntity, String> {
}