package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.RelationshipId
import org.springframework.data.neo4j.core.schema.RelationshipProperties
import org.springframework.data.neo4j.core.schema.TargetNode

@RelationshipProperties
class RecipeRequires(
        @TargetNode
        val item: ItemDescriptorEntity,
        val outputPerCycle: Double,
) {
    @RelationshipId
    var id: Long? = null
}

@RelationshipProperties
class ItemDescriptorProducedBy(
        @TargetNode
        val recipe: RecipeEntity,
        val outputPerCycle: Double,
) {
    @RelationshipId
    var id: Long? = null
}
@RelationshipProperties
class RecipeProducing(
        @TargetNode
        val item: ItemDescriptorEntity,
        val outputPerCycle: Double,
) {
    @RelationshipId
    var id: Long? = null
}
@RelationshipProperties
class PowerGeneratorRequires(
        @TargetNode
        val item: ItemDescriptorEntity,
        val outputPerCycle: Double,
) {
    @RelationshipId
    var id: Long? = null
}
@RelationshipProperties
class PowerGeneratorProduces(
        @TargetNode
        val item: ItemDescriptorEntity,
        val outputPerCycle: Double,
) {
    @RelationshipId
    var id: Long? = null
}

@RelationshipProperties
class PowerGeneratorFuelRequires(
        @TargetNode
        val item: ItemDescriptorEntity,
        val burnTime: Double,
        val inputPerCycle: Double,
) {
    @RelationshipId
    var id: Long? = null
}