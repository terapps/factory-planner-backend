package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import org.springframework.data.neo4j.core.schema.RelationshipProperties
import java.util.*

@RelationshipProperties
 class Unlocks(
)
data class Schematic(
        val className: String?,
        val displayName: String?,
        val description: String?,
        val type: String?, // TODO enum
        val tier: Int?,
        val timeToComplete: Float?,
        // TODO schem->unlocks->Recipe|Research...

        @Relationship(type = "REQUIRED_IN", direction = Relationship.Direction.INCOMING)
        var unlocks: Set<RecipeRequires> = emptySet(),
) {
    @Id
    @GeneratedValue
    lateinit var id: UUID

    var weightedPoints: Float = Float.MAX_VALUE
    var energyPoints: Float = Float.MAX_VALUE
    var buildingCountPoints: Float = Float.MAX_VALUE
    var sinkingPoints: Float = Float.MAX_VALUE
}