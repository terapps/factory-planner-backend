package terapps.factoryplanner.core.repositories

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.stereotype.Repository
import terapps.factoryplanner.core.entities.Schematic

@Repository
interface SchematicRepository : Neo4jRepository<Schematic, String> {
        @Query("MATCH (p:Schematic) RETURN MAX(p.tier) AS tier")
        fun findMaxTier(): Int
}