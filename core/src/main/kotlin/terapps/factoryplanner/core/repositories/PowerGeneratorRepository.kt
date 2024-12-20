package terapps.factoryplanner.core.repositories

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository
import terapps.factoryplanner.core.entities.ExtractorEntity
import terapps.factoryplanner.core.entities.PowerGeneratorEntity
import java.util.*

@Repository
interface PowerGeneratorRepository : Neo4jRepository<PowerGeneratorEntity, String> {

}