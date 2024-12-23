package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.*
import org.springframework.data.neo4j.core.support.UUIDStringGenerator
import terapps.factoryplanner.core.projections.RecipeSummary
import terapps.factoryplanner.core.projections.TaskIoSummary

enum class TaskStatus {
    ToDo,
    InProgress,
    Disconnected,
    Done,
}

enum class TaskEnum {
    CraftingSite,
    ExtractingSite,
    PowerGeneratingSite
}


data class ExtractingNode(
        val requiredCycles: Double,
        val purity: String, // TODO purity enum
        val overclockingProfile: Double
)

@Node("TaskGroup")
class TaskGroupEntity(
        var name: String,

        @Relationship(type = "TASK_MAPPED_BY", direction = Relationship.Direction.INCOMING)
        var tasks: Set<TaskEntity> = emptySet()
) {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator::class)
    lateinit var id: String
}

@Node("TaskIO")
class TaskIoEntity(
        val amountPerMinute: Double,
        ) {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator::class)
    lateinit var id: String

    @Relationship(type = "TASKIO_ITEM", direction = Relationship.Direction.INCOMING)
    var item: ItemDescriptorEntity? = null

    @Relationship(type = "INCOMING_TASK", direction = Relationship.Direction.INCOMING)
    var source: TaskEntity? = null

    @Relationship(type = "OUTGOING_TASK", direction = Relationship.Direction.OUTGOING)
    var destination: TaskEntity? = null
}

@Node("Task")
class TaskEntity {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator::class)
    lateinit var id: String

    lateinit var status: TaskStatus

    lateinit var type: TaskEnum

    @Relationship(type = "INCOMING_TASK", direction = Relationship.Direction.INCOMING)
    var ingoing: Set<TaskIoEntity> = emptySet()

    @Relationship(type = "OUTGOING_TASK", direction = Relationship.Direction.OUTGOING)
    var outgoing: Set<TaskIoEntity> = emptySet()

    // CraftingSite
    var requiredCycles: Double? = null

    @Relationship(type = "TASK_MANUFACTURED_BY", direction = Relationship.Direction.INCOMING)
    var recipe: RecipeEntity? = null

    var overclockingProfile: Double? = null

    // ExtractingSite
    @Relationship(type = "TASK_MANUFACTURED_BY", direction = Relationship.Direction.INCOMING)
    var extractor: ExtractorEntity? = null

    // PowerGeneratingSite
    @Relationship(type = "TASK_POWER_GENERATED_BY", direction = Relationship.Direction.INCOMING)
    var powerGenerator: PowerGeneratorEntity? = null

    var powerProducedInMegaWatt: Double? = null
}





