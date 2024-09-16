package terapps.factoryplanner.core.repositories

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import terapps.factoryplanner.core.entities.Recipe
import terapps.factoryplanner.core.projections.RecipeProducingSummary
import java.util.*

@Repository
interface RecipeRepository : Neo4jRepository<Recipe, UUID> {
    fun findByClassName(className: String): RecipeProducingSummary?

    fun findByIdAndClassName()

    fun findAllByUnlockedByTierLessThanEqualAndProducingItemClassName(tier: Int, producedItemClassName: String)

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