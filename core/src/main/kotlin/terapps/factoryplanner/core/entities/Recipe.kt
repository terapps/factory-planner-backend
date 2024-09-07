package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@Node
data class Recipe(
        @Id
        val id: String,
        val displayName: String,
        val manufacturingDuration: Float,
        @Relationship(type = "PRODUCED_IN", direction = Relationship.Direction.OUTGOING)
        val producedIn: CraftingMachine?,
        @Relationship(type = "EXTRACTED_IN", direction = Relationship.Direction.OUTGOING)
        val extractedIn: Set<Extractor>,
        @Relationship(type = "REQUIRES", direction = Relationship.Direction.INCOMING)
        var ingredients: Set<RecipeRequires> = emptySet(),
/*
        @Relationship(type = "PRODUCED_BY", direction = Relationship.Direction.OUTGOING)
        var producing: Set<RecipeProducing> = emptySet()
*/
) {
    var weightedPoints: Float = Float.MAX_VALUE
    var energyPoints: Float = Float.MAX_VALUE
    var buildingCountPoints: Float = Float.MAX_VALUE
    var sinkingPoints: Float = Float.MAX_VALUE
}

@Repository
interface RecipeRepository : Neo4jRepository<Recipe, String> {

}
