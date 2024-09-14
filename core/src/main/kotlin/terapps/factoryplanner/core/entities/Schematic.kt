package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.*
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository
import java.util.*

data class Schematic(
        // TODO is unlocked
        // MVP : consider is unlocked if tier <= game tier info (fetch http api)
        // Long term : parse save file and maintain an "unlocked status"
        // Will imply a "GameProfile" entity for runtime save info.
        val className: String?,
        val displayName: String?,
        val description: String?,
        val type: String?, // TODO enum
        val tier: Int?,
        val timeToComplete: Float?,
        @Relationship(type = "SCHEMATIC_UNLOCKS_RECIPE", direction = Relationship.Direction.OUTGOING)
        var unlocksRecipe: Set<SchematicUnlocksRecipe> = emptySet(),
        @Relationship(type = "SCHEMATIC_UNLOCKS_SCHEMATIC", direction = Relationship.Direction.OUTGOING)
        var unlocksSchematic: Set<SchematicUnlocksSchematic> = emptySet(), // TODO should be inverse of schematic dependencies, do i need to map it here
        @Relationship(type = "SCHEMATIC_UNLOCKS_RESOURCE", direction = Relationship.Direction.OUTGOING)
        var unlocksResources: Set<SchematicUnlocksResource> = emptySet(), // TODO should be inverse of schematic dependencies, do i need to map it here
        @Relationship(type = "SCHEMATIC_DEPENDENCY", direction = Relationship.Direction.OUTGOING)
        var depdendsOn: Set<SchematicDependency> = emptySet(), // TODO should be inverse of schematic dependencies, do i need to map it here

) {
    @Id
    @GeneratedValue
    lateinit var id: UUID
}

@Repository
interface SchematicRepository : Neo4jRepository<Schematic, UUID> {
}

