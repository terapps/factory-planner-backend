package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import java.util.*

@Node
data class Recipe(
        val className: String?,
        val displayName: String?,
        val manufacturingDuration: Float?,
        @Relationship(type = "REQUIRED_IN", direction = Relationship.Direction.OUTGOING)
        var ingredients: Set<RecipeRequires> = emptySet(),
        @Relationship(type = "MANUFACTURED_IN", direction = Relationship.Direction.OUTGOING)
        val manufacturedIn: Set<CraftingMachine> = emptySet(),
        @Relationship(type = "PRODUCED_BY", direction = Relationship.Direction.INCOMING)
        var producing: Set<RecipeProducing> = emptySet(),
        @Relationship(type = "SCHEMATIC_UNLOCKS_RECIPE", direction = Relationship.Direction.INCOMING)
        var unlockedBy: Set<Schematic> = emptySet(),
        ) {
    @Id
    @GeneratedValue
    lateinit var id: UUID
}

