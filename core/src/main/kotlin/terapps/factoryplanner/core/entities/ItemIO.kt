package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.RelationshipId
import org.springframework.data.neo4j.core.schema.RelationshipProperties
import org.springframework.data.neo4j.core.schema.TargetNode

@RelationshipProperties
class RecipeRequires(
        @TargetNode
        val descriptor: ItemDescriptor,
        val outputPerCycle: Float,
) {
    @RelationshipId
    var id: Long? = null
}

@RelationshipProperties
class ItemDescriptorProducedBy(
        @TargetNode
        val recipe: Recipe,
        val outputPerCycle: Float,
) {
    @RelationshipId
    var id: Long? = null
}
@RelationshipProperties
class RecipeProducing(
        @TargetNode
        val item: ItemDescriptor,
        val outputPerCycle: Float,
) {
    @RelationshipId
    var id: Long? = null
}
