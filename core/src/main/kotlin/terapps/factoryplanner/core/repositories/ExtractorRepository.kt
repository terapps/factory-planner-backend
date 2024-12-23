package terapps.factoryplanner.core.repositories

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository
import terapps.factoryplanner.core.entities.ExtractorEntity
import java.util.*

@Repository
interface ExtractorRepository : Neo4jRepository<ExtractorEntity, String> {
        fun existsByClassName(id: String): Boolean
        fun findByClassName(id: String): ExtractorEntity?
        fun findAllByClassNameIn(classNames: List<String>): Collection<ExtractorEntity>
}