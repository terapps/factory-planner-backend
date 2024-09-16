package terapps.factoryplanner.core.repositories

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository
import terapps.factoryplanner.core.entities.Extractor
import java.util.*

@Repository
interface ExtractorRepository : Neo4jRepository<Extractor, UUID> {
        fun findByClassName(id: String): Extractor?
        fun findAllByClassNameIn(classNames: List<String>): Collection<Extractor>
}