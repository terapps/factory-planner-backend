package terapps.factoryplanner.core.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import terapps.factoryplanner.core.entities.*
import java.util.*

enum class TaskEnum {
    CraftingSite,
    ExtractingSite,
    PowerGeneratingSite
}

fun TaskEntity.toDto(): TaskDto {
    return when (this) {
        is CraftingSiteTaskEntity -> CraftingSiteTaskDto(this)
        is ExtractingSiteTaskEntity -> ExtractingSiteTaskDto(this)
        is PowerGeneratingTaskEntity -> PowerGeneratingSiteTaskDto(this)
        else -> throw Error("Unknown task")
    }
}

class TaskGroupDto(
        var name: String,
        var tasks: Collection<TaskDto> = emptySet()
) {
    lateinit var id: String

    constructor(taskGroupEntity: TaskGroupEntity) : this(
            taskGroupEntity.name,
            taskGroupEntity.tasks.map { it.toDto() }) {
        id = taskGroupEntity.id.toString()
    }
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
abstract class TaskDto(
        val type: TaskEnum,
        val status: TaskStatus,
        val ingoing: Collection<TaskDto>,
        val outgoing: Collection<TaskDto>
) {
    abstract var id: String
}

class CraftingSiteTaskDto(
        status: TaskStatus,
        ingoing: Collection<TaskDto>,
        outgoing: Collection<TaskDto>,
        val requiredCycles: Double,
        val recipe: RecipeProducingDto,
        val overclockingProfile: Double
) : TaskDto(TaskEnum.CraftingSite, status, ingoing, outgoing) {
    override lateinit var id: String

    constructor(taskEntity: CraftingSiteTaskEntity) : this(
            taskEntity.status,
            taskEntity.ingoing.map { it.toDto() },
            taskEntity.outgoing.map { it.toDto() },
            taskEntity.requiredCycles,
            RecipeProducingDto(taskEntity.recipe),
            taskEntity.overclockingProfile
    ) {
        id = taskEntity.id.toString()
    }
}

class ExtractingSiteTaskDto(
        status: TaskStatus,
        ingoing: Collection<TaskDto>,
        outgoing: Collection<TaskDto>,
        val nodes: Collection<ExtractingNode>,
        val item: ItemDescriptorDto,
        val extractor: ExtractorDto,
) : TaskDto(TaskEnum.ExtractingSite, status, ingoing, outgoing) {
    override lateinit var id: String

    constructor(taskEntity: ExtractingSiteTaskEntity) : this(
            taskEntity.status,
            taskEntity.ingoing.map { it.toDto() },
            taskEntity.outgoing.map { it.toDto() },
            taskEntity.nodes,
            ItemDescriptorDto(taskEntity.item),
            ExtractorDto(taskEntity.extractor),
    ) {
        id = taskEntity.id.toString()
    }
}

class PowerGeneratingSiteTaskDto(
        status: TaskStatus,
        ingoing: Collection<TaskDto>,
        outgoing: Collection<TaskDto>,
        val selectedFuel: ItemDescriptorDto,
        val powerGeneratorDto: PowerGeneratorDto,
        val powerProducedInMegaWatt: Double
) : TaskDto(TaskEnum.PowerGeneratingSite, status, ingoing, outgoing) {
    override lateinit var id: String

    constructor(taskEntity: PowerGeneratingTaskEntity) : this(
            taskEntity.status,
            taskEntity.ingoing.map { it.toDto() },
            taskEntity.outgoing.map { it.toDto() },
            ItemDescriptorDto(taskEntity.selectedFuel),
            PowerGeneratorDto(taskEntity.powerGenerator),
            taskEntity.powerProducedInMegaWatt
    ) {
        id = taskEntity.id.toString()
    }
}