package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository
import java.util.*

@Node
data class Extractor(
        override val className: String,
        val displayName: String,
        val description: String,
        val extractCycleTime: Float,
        val itemsPerCycle: Int,
        val powerConsumption: Float,
        val powerConsumptionExponent: Float,
        override val minPotential: Float,
        override val maxPotential: Float,
        override val maxPotentialIncreasePerCrystal: Float,
        val extractorType: String, // TODO types enum?
        val allowedResourceForm: Set<String>,
        val allowedResources: Set<String> // i dont want this since its going to end up a relationship
): Automaton {
        @Id
        @GeneratedValue
        lateinit var id: UUID
}

// TODO input/output info is empty in file, see in later version


@Repository
interface ExtractorRepository : Neo4jRepository<Extractor, UUID> {
        fun findByClassName(id: String): Extractor?
        fun findAllByClassNameIn(classNames: List<String>): Collection<Extractor>
}
