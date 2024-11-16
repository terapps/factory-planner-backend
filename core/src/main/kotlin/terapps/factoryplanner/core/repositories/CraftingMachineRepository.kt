package terapps.factoryplanner.core.repositories

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository
import terapps.factoryplanner.core.entities.CraftingMachineEntity
import java.util.*

@Repository
interface CraftingMachineRepository : Neo4jRepository<CraftingMachineEntity, String> {
}