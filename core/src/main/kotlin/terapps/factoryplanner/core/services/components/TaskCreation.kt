package terapps.factoryplanner.core.services.components

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import terapps.factoryplanner.core.dto.*
import terapps.factoryplanner.core.entities.*

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = TaskCreationDto::class,
        visible = false
)
@JsonSubTypes(
        value = [
            JsonSubTypes.Type(value = CraftingSiteTaskCreationDto::class, name = "CraftingSite"),
            JsonSubTypes.Type(value = ExtractingSiteTaskCreationDto::class, name = "ExtractorSite"),
            JsonSubTypes.Type(value = PowerGeneratingSiteTaskCreationDto::class, name = "PowerGeneratingSite"),
        ],
)
abstract class TaskCreationDto(
        val type: TaskEnum,
        val status: TaskStatus,
) {
}

class CraftingSiteTaskCreationDto(
        val requiredCycles: Double,
        val recipe: String,
        val overclockingProfile: Double
) : TaskCreationDto(TaskEnum.CraftingSite, TaskStatus.ToDo) {

}

class ExtractingSiteTaskCreationDto(
        status: TaskStatus,
        val nodes: Collection<ExtractingNode>,
        val item: String,
        val extractor: String,
) : TaskCreationDto(TaskEnum.ExtractingSite, TaskStatus.ToDo) {

}

class PowerGeneratingSiteTaskCreationDto(
        status: TaskStatus,
        val selectedFuel: String,
        val powerGeneratorDto: String,
        val powerProducedInMegaWatt: Double
) : TaskCreationDto(TaskEnum.PowerGeneratingSite, TaskStatus.ToDo) {
}