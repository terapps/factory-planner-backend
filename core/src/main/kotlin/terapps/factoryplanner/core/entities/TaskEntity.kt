package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.GeneratedValue
import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import org.springframework.data.neo4j.core.support.UUIDStringGenerator
import terapps.factoryplanner.core.projections.ItemDescriptorSummary
import terapps.factoryplanner.core.projections.RecipeProducingSummary
import java.util.UUID

enum class TaskStatus {
    ToDo,
    InProgress,
    Disconnected,
    Done,
}

data class ExtractingNode(
        val requiredCycles: Double,
        val purity: String, // TODO purity enum
        val overclockingProfile: Double
)

@Node("task_group")
class TaskGroupEntity(
        var name: String,

        @Relationship(type = "TASK_MAPPED_BY", direction = Relationship.Direction.INCOMING)
        var tasks: Set<TaskEntity> = emptySet()
) {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator::class)
    lateinit var id: String

}

abstract class TaskEntity(

) {
    @Id
    @GeneratedValue(generatorClass = UUIDStringGenerator::class)
    lateinit var id: String

    lateinit var status: TaskStatus

    @Relationship(type = "INCOMING_TASK", direction = Relationship.Direction.INCOMING)
    var ingoing: Set<TaskEntity> = emptySet()

    @Relationship(type = "OUTGOING_TASK", direction = Relationship.Direction.OUTGOING)
    var outgoing: Set<TaskEntity> = emptySet()
}

@Node("CraftingSiteTask")
class CraftingSiteTaskEntity(
        val requiredCycles: Double,

        @Relationship(type = "TASK_MANUFACTURED_BY", direction = Relationship.Direction.INCOMING)
        val recipe: RecipeProducingSummary,

        val overclockingProfile: Double
) : TaskEntity() {
}

@Node("ExtractingSiteTask")
class ExtractingSiteTaskEntity(
        val nodes: Collection<ExtractingNode>,

        @Relationship(type = "TASK_MANUFACTURING", direction = Relationship.Direction.OUTGOING)
        val item: ItemDescriptorSummary,

        @Relationship(type = "TASK_MANUFACTURED_BY", direction = Relationship.Direction.INCOMING)
        val extractor: ExtractorEntity,
) : TaskEntity()

@Node("PowerGeneratingSiteTask")
class PowerGeneratingTaskEntity(
        val selectedFuel: ItemDescriptorSummary,

        @Relationship(type = "TASK_POWER_GENERATED_BY", direction = Relationship.Direction.INCOMING)
        val powerGenerator: PowerGeneratorEntity,

        val powerProducedInMegaWatt: Double,
) : TaskEntity()





