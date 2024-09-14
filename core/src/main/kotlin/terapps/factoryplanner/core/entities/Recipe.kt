package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import terapps.factoryplanner.core.projections.RecipeProducingSummary
import terapps.factoryplanner.core.projections.RecipeRequiresSummary
import java.util.*

@Node
data class Recipe(
        val className: String?,
        val displayName: String?,
        val manufacturingDuration: Float?,
        @Relationship(type = "REQUIRED_IN", direction = Relationship.Direction.INCOMING)
        var ingredients: Set<RecipeRequires> = emptySet(),
        @Relationship(type = "MANUFACTURED_IN", direction = Relationship.Direction.INCOMING)
        val manufacturedIn: Set<CraftingMachine> = emptySet(),
        @Relationship(type = "PRODUCED_BY", direction = Relationship.Direction.OUTGOING)
        var producing: Set<RecipeProducing> = emptySet()
) {
    @Id
    @GeneratedValue
    lateinit var id: UUID
}

@Repository
interface RecipeRepository : Neo4jRepository<Recipe, UUID> {
    fun findByClassName(className: String): RecipeRequiresSummary?

    fun findByProducingItemClassName(itemClass: String): Collection<RecipeProducingSummary>

    @Query("MATCH (r: Recipe {className: \$className}) " +
            "SET r.weightedPoints = \$weightedPoints " +
            "SET r.energyPoints = \$energyPoints " +
            "SET r.buildingCountPoints = \$buildingCountPoints " +
            "SET r.sinkingPoints = \$sinkingPoints " +
            "RETURN r")
    fun updateWeight(
            @Param("className") className: String,
            @Param("weightedPoints") weightedPoints: Float,
            @Param("energyPoints") energyPoints: Float,
            @Param("buildingCountPoints") buildingCountPoints: Float,
            @Param("sinkingPoints") sinkingPoints: Float): Recipe

}
