package terapps.factoryplanner.core.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import terapps.factoryplanner.core.entities.*
import terapps.factoryplanner.core.projections.*
import java.util.*

class TaskLinkDto(
        val id: String,
        val status: TaskStatus,
        val type: TaskEnum,
) {
    constructor(task: TaskMinimalSummary): this(
            task.getId(),
            task.getStatus(),
            task.getType()
    )
}


class TaskGroupDto(
        var name: String,
        val tasks: Collection<TaskLinkDto>
) {
    lateinit var id: String

    constructor(taskGroupEntity: TaskGroupSummary) : this(
            taskGroupEntity.getName(),
            taskGroupEntity.getTasks().map { TaskLinkDto(it) }
    ) {
        id = taskGroupEntity.getId()
    }
}

class TaskIoDto(
        val id: String,
        val amountPerMinute: Double,
        val item: ItemDescriptorMetadataDto,
        val source: TaskLinkDto? = null,
        val destination: TaskLinkDto? = null
) {
    constructor(taskIoSummary: TaskIoSummary): this(
            taskIoSummary.getId(),
            taskIoSummary.getAmountPerMinute(),
            ItemDescriptorMetadataDto(taskIoSummary.getItem()),
            taskIoSummary.getSource()?.let { TaskLinkDto(it) },
            taskIoSummary.getDestination()?.let { TaskLinkDto(it) },
    )

}

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = TaskDto::class,
        visible = false
)
@JsonSubTypes(
        value = [
            JsonSubTypes.Type(value = CraftingSiteTaskDto::class, name = "CraftingSite"),
            JsonSubTypes.Type(value = ExtractingSiteTaskDto::class, name = "ExtractorSite"),
            JsonSubTypes.Type(value = PowerGeneratingSiteTaskDto::class, name = "PowerGeneratingSite"),
        ],
)
open class TaskDto(
        val type: TaskEnum,
        val status: TaskStatus,
        val ingoing: Collection<TaskIoDto> = emptySet(),
        val outgoing: Collection<TaskIoDto> = emptySet()
) {
    lateinit var id: String
}

class CraftingSiteTaskDto(
        status: TaskStatus,
        ingoing: Collection<TaskIoDto>,
        outgoing: Collection<TaskIoDto>,
        val requiredCycles: Double,
        val recipe: RecipeDto,
        val overclockingProfile: Double
) : TaskDto(TaskEnum.CraftingSite, status, ingoing, outgoing) {
    constructor(taskEntity: CraftingSiteTaskSummary) : this(
            taskEntity.getStatus(),
            taskEntity.getIngoing().map { TaskIoDto(it) },
            taskEntity.getOutgoing().map { TaskIoDto(it) },
            taskEntity.getRequiredCycles(),
            RecipeDto(taskEntity.getRecipe()),
            taskEntity.getOverclockingProfile()
    ) {
        id = taskEntity.getId()
    }
}

class ExtractingSiteTaskDto(
        status: TaskStatus,
        ingoing: Collection<TaskIoDto>,
        outgoing: Collection<TaskIoDto>,
        val extractor: ExtractorDto,
) : TaskDto(TaskEnum.ExtractingSite, status, ingoing, outgoing) {

    constructor(taskEntity: ExtractingSiteTaskSummary) : this(
            taskEntity.getStatus(),
            taskEntity.getIngoing().map { TaskIoDto(it) },
            taskEntity.getOutgoing().map { TaskIoDto(it) },
            ExtractorDto(taskEntity.getExtractor()),
    ) {
        id = taskEntity.getId()
    }
}

class PowerGeneratingSiteTaskDto(
        status: TaskStatus,
        ingoing: Collection<TaskIoDto>,
        outgoing: Collection<TaskIoDto>,
        val powerGeneratorDto: PowerGeneratorDto,
        val powerProducedInMegaWatt: Double
) : TaskDto(TaskEnum.PowerGeneratingSite, status, ingoing, outgoing) {
    constructor(taskEntity: PowerGeneratingSiteTaskSummary) : this(
            taskEntity.getStatus(),
            taskEntity.getIngoing().map { TaskIoDto(it) },
            taskEntity.getOutgoing().map { TaskIoDto(it) },
            PowerGeneratorDto(taskEntity.getPowerGenerator()),
            taskEntity.getPowerProducedInMegaWatt()
    ) {
        id = taskEntity.getId()
    }
}