package terapps.factoryplanner.core.entities

import org.neo4j.ogm.annotation.EndNode
import org.neo4j.ogm.annotation.RelationshipEntity
import org.neo4j.ogm.annotation.StartNode
import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id

@RelationshipEntity(type = "PRODUCES")
class RecipeProduces(
        @StartNode
        val descriptor: ItemDescriptor,
        val outputPerCycle: Float,
) {
    @EndNode
    lateinit var recipe: Recipe

    @Id
    @GeneratedValue
    var id: Long? = null
}

@RelationshipEntity(type = "REQUIRES")
class RecipeRequires(
        @StartNode
        val descriptor: ItemDescriptor,
        val outputPerCycle: Float,
) {
    @EndNode
    lateinit var recipe: Recipe

    @Id
    @GeneratedValue
    var id: Long? = null
}