package terapps.factoryplanner.core.entities

import org.neo4j.ogm.annotation.NodeEntity
import org.springframework.data.neo4j.core.schema.*
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.stereotype.Repository

@NodeEntity
data class Recipe(
        @Id
        val id: String,
        val displayName: String,
        val manufacturingDuration: Float,
        @Relationship(type = "REQUIRES", direction = Relationship.Direction.INCOMING)
        val ingredients: Set<RecipeRequires>,
        @Relationship(type = "PRODUCES", direction = Relationship.Direction.OUTGOING)
        val produces: Set<RecipeProduces>,
        @Relationship(type = "PRODUCED_IN", direction = Relationship.Direction.OUTGOING)
        val producedIn: CraftingMachine?,
        @Relationship(type = "EXTRACTED_IN", direction = Relationship.Direction.OUTGOING)
        val extractedIn: Set<Extractor>,
) {
    var weightedPoints: Float = Float.MAX_VALUE
    var energyPoints: Float = Float.MAX_VALUE
    var buildingCountPoints: Float = Float.MAX_VALUE
    var sinkingPoints: Float = Float.MAX_VALUE
}

@Repository
interface RecipeRepository : Neo4jRepository<Recipe, String> {
    @Query("MATCH (r:Recipe)-[p:PRODUCES]->(:ItemDescriptor {id: \$itemDescriptorId}) RETURN r.id")
    fun findRecipeIdsByItemDescriptorId(itemDescriptorId: String): Collection<String>
    fun findByIdIn(ids: Collection<String>): Collection<Recipe>


}
