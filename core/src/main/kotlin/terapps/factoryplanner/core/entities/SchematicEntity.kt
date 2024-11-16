package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship

@Node("Schematic")
data class SchematicEntity(
        // TODO is unlocked
        // MVP : consider is unlocked if tier <= game tier info (fetch http api)
        // Long term : parse save file and maintain an "unlocked status"
        // Will imply a "GameProfile" entity for runtime save info.
        @Id
        val className: String,
        val displayName: String? = null,
        val description: String? = null,
        val type: String? = null, // TODO enum
        val tier: Int? = null,
        val timeToComplete: Double? = null,
        @Relationship(type = "SCHEMATIC_UNLOCKS_RECIPE", direction = Relationship.Direction.OUTGOING)
        var unlocksRecipe: Set<SchematicUnlocksRecipe> = emptySet(),
        @Relationship(type = "SCHEMATIC_UNLOCKS_RESOURCE", direction = Relationship.Direction.OUTGOING)
        var unlocksResources: Set<SchematicUnlocksResource> = emptySet(), // TODO should be inverse of schematic dependencies, do i need to map it here
        @Relationship(type = "SCHEMATIC_DEPENDENCY", direction = Relationship.Direction.OUTGOING)
        var depdendsOn: Set<SchematicDependencyEntity> = emptySet(), // TODO should be inverse of schematic dependencies, do i need to map it here

) {
}

