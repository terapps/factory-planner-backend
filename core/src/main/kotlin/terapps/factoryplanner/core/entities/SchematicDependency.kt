package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import java.util.*

@Node
data class SchematicDependency(
        val className: String, // BP_SchematicPurchasedDependency_C
        val requiresAll: Boolean,
        @Relationship(type = "SCHEMATIC_DEPENDS_ON", direction = Relationship.Direction.OUTGOING)
        val schematics: Set<Schematic> = emptySet(),
) {
        @Id
        @GeneratedValue
        lateinit var id: UUID
}