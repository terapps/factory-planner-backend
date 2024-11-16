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


@Node("ItemDescriptor")
class ItemDescriptorEntity(
        @Id
        val className: String,
        val displayName: String?,
        val description: String?,
        val category: ItemCategory?,
        val sinkablePoints: Int? = null,
        val iconSmall: String? = null,
        val iconPersistent: String? = null,
        val energyValue: Double? = null,
        val form: String? = null, // TODO enum
        val powershardType: String? = null,
        val extraPotential: Double? =null,
        val extraProductionBoost: Double? =null,
        @Relationship(type = "EXTRACTED_IN", direction = Relationship.Direction.OUTGOING)
        val extractedIn: Set<ExtractorEntity> = emptySet(),
) {
}

