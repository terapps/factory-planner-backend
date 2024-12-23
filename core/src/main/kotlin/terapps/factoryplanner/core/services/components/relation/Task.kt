package terapps.factoryplanner.core.services.components.relation

import org.springframework.data.neo4j.core.Neo4jClient
import org.springframework.stereotype.Component
import terapps.factoryplanner.core.entities.*
import terapps.factoryplanner.core.services.components.Relationship
import terapps.factoryplanner.core.services.components.RelationshipQuery
import terapps.factoryplanner.core.services.components.getEntityName

@Component
class RecipeToTask(override var neo4jClient: Neo4jClient, override var relationships: List<Relationship>) : RelationshipQuery(
        getEntityName(RecipeEntity::class) to getEntityName(TaskEntity::class),
        "TASK_MANUFACTURED_BY"
) {
    override val destIdProp: String = "id"
}

@Component
class TaskToGroup(override var neo4jClient: Neo4jClient, override var relationships: List<Relationship>) : RelationshipQuery(
        getEntityName(TaskEntity::class) to getEntityName(TaskGroupEntity::class),
        "TASK_MAPPED_BY"
) {
    override val sourceIdProp: String = "id"
    override val destIdProp: String = "id"
}

@Component
class ExtractorToTask(override var neo4jClient: Neo4jClient, override var relationships: List<Relationship>) : RelationshipQuery(
        getEntityName(ExtractorEntity::class) to getEntityName(TaskEntity::class),
        "TASK_MANUFACTURED_BY"
) {
    override val destIdProp: String = "id"
}

@Component
class PowerGeneratorToTask(override var neo4jClient: Neo4jClient, override var relationships: List<Relationship>) : RelationshipQuery(
        getEntityName(PowerGeneratorEntity::class) to getEntityName(TaskEntity::class),
        "TASK_POWER_GENERATED_BY"
) {
    override val destIdProp: String = "id"
}

@Component
class ItemToTaskIo(override var neo4jClient: Neo4jClient) : RelationshipQuery(
        getEntityName(ItemDescriptorEntity::class) to getEntityName(TaskIoEntity::class),
        "TASKIO_ITEM"
) {
    override var relationships: List<Relationship> = mutableListOf()

    override val destIdProp: String = "id"
}

@Component
class IoToTask(override var neo4jClient: Neo4jClient) : RelationshipQuery(
        getEntityName(TaskIoEntity::class) to getEntityName(TaskEntity::class),
        "INCOMING_TASK"
) {
    override var relationships: List<Relationship> = mutableListOf()

    override val sourceIdProp: String = "id"
    override val destIdProp: String = "id"
}

@Component
class TaskToIo(override var neo4jClient: Neo4jClient) : RelationshipQuery(
        getEntityName(TaskEntity::class) to getEntityName(TaskIoEntity::class),
        "OUTGOING_TASK"
) {
    override var relationships: List<Relationship> = mutableListOf()

    override val sourceIdProp: String = "id"
    override val destIdProp: String = "id"
}