package terapps.factoryplanner.core.repositories

import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import terapps.factoryplanner.core.entities.ItemCategory
import terapps.factoryplanner.core.entities.RecipeEntity
import java.util.*

@Repository
interface RecipeRepository : Neo4jRepository<RecipeEntity, String> {
    fun <T> findByClassName(className: String, clazz: Class<T>): T?
    fun existsByClassName(className: String): Boolean

    //AndUnlockedByTierLessThanEqual
    //, unlockedbyTier: Int
    fun <T> findByProducingItemClassName(producingItemClassname: String, clazz: Class<T>): Collection<T>

    fun <T> findAllByProducingItemCategory(category: ItemCategory, clazz: Class<T>): Collection<T>
    fun <T> findAllByIngredientsItemCategory(category: ItemCategory, clazz: Class<T>): Collection<T>

    fun <T> findAllByProducingItemClassNameIn(producingItemClassName: Collection<String>, clazz: Class<T>): Collection<T>
    fun <T> findAllByIngredientsItemClassNameIn(producingItemClassName: Collection<String>, clazz: Class<T>): Collection<T>

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
            @Param("sinkingPoints") sinkingPoints: Float): RecipeEntity

}