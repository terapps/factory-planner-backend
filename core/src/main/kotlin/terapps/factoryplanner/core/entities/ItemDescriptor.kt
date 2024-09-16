package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import java.util.*

enum class ItemCategory {
    Raw,
    Biomass,
    Building,
    Equipment,
    Craftable,
    Vehicle,
    Consumable,
    PowerShard
}

@Node
data class ItemDescriptor(
        val className: String?,
        val displayName: String?,
        val description: String?,
        val category: ItemCategory?,
        val sinkablePoints: Int? = null,
        val energyValue: Float? = null,
        val form: String? = null, // TODO enum
        val powershardType: String? = null,
        val extraPotential: Float? =null,
        val extraProductionBoost: Float? =null,
        @Relationship(type = "EXTRACTED_IN", direction = Relationship.Direction.OUTGOING)
        val extractedIn: Set<Extractor> = emptySet(),
        @Relationship(type = "PRODUCED_BY", direction = Relationship.Direction.OUTGOING)
        var producedBy: Set<ItemDescriptorProducedBy> = emptySet()
) {
    @Id
    @GeneratedValue
    lateinit var id: UUID
}

