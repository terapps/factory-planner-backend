package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.RelationshipId
import org.springframework.data.neo4j.core.schema.RelationshipProperties
import org.springframework.data.neo4j.core.schema.TargetNode


@RelationshipProperties
class SchematicUnlocksRecipe(
        val className: String,
        @TargetNode
        val recipe: Recipe,
) {
        @RelationshipId
        var id: Long? = null
}

@RelationshipProperties
class SchematicUnlocksSchematic(
        val className: String,
        @TargetNode
        val schematic: Schematic,
){
        @RelationshipId
        var id: Long? = null
}

@RelationshipProperties
class SchematicUnlocksResource(
        val className: String,
        @TargetNode
        val item: ItemDescriptor,
){
        @RelationshipId
        var id: Long? = null
}