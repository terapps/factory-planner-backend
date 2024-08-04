package terapps.factoryplanner.entities.satisfactory

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.RelationshipProperties
import org.springframework.data.neo4j.core.schema.TargetNode

@RelationshipProperties()
class ItemIO(
        @TargetNode
        val descriptor: ItemDescriptor,
        val outputPerCycle: Float,
) {
    @Id
    @GeneratedValue
    var id: Long? = null
}