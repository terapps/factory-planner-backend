package terapps.factoryplanner.entities.satisfactory

import org.neo4j.ogm.annotation.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@NodeEntity
data class CraftingMachine(
        @Id
        @GeneratedValue()
        val id: String,
        val displayName: String,
        val description: String,
        val manufacturingSpeed: Float = 0f,
        val powerConsumption: Float = 0f,
        val powerConsumptionExponent: Float = 0f,
        val minPotential: Float = 0f,
        val maxPotential: Float = 0f,
        val maxPotentialIncreasePerCrystal: Float = 0f,
)
// TODO input/output info is empty in file, see in later version


@Repository
interface CraftingMachineRepository : Neo4jRepository<CraftingMachine, String> {
}
