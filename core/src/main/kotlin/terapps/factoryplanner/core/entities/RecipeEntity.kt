package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import java.util.*

@Node("Recipe")
class RecipeEntity(
        @Id
        val className: String,
        val displayName: String?,
        val manufacturingDuration: Double?,
        @Relationship(type = "REQUIRED_IN", direction = Relationship.Direction.INCOMING)
        var ingredients: Set<RecipeRequires> = emptySet(),
        @Relationship(type = "MANUFACTURED_IN", direction = Relationship.Direction.OUTGOING)
        val manufacturedIn: Set<CraftingMachineEntity> = emptySet(),
        @Relationship(type = "PRODUCED_BY", direction = Relationship.Direction.OUTGOING)
        var producing: Set<RecipeProducing> = emptySet(),
        @Relationship(type = "SCHEMATIC_UNLOCKS_RECIPE", direction = Relationship.Direction.INCOMING)
        var unlockedBy: Set<SchematicEntity> = emptySet(),
        ) {
}

