package terapps.factoryplanner.core.projections

import terapps.factoryplanner.core.entities.ExtractorEntity
import terapps.factoryplanner.core.entities.TaskEnum
import terapps.factoryplanner.core.entities.TaskStatus

interface TaskGroupSummary {
    fun getId(): String
    fun getName(): String
    fun getTasks(): Set<TaskMinimalSummary>
}

interface TaskMinimalSummary {
    fun getId(): String
    fun getStatus(): TaskStatus
    fun getType(): TaskEnum
}

interface TaskSummary {
    fun getId(): String
    fun getStatus(): TaskStatus
    fun getType(): TaskEnum
    fun getIngoing(): Set<TaskIoSummary>
    fun getOutgoing(): Set<TaskIoSummary>
}

interface TaskIoSummary {
    fun getId(): String
    fun getItem(): ItemDescriptorMetadata
    fun getSource(): TaskMinimalSummary?
    fun getDestination(): TaskMinimalSummary?
    fun getAmountPerMinute(): Double
}

interface CraftingSiteTaskSummary : TaskSummary {
    fun getRecipe(): RecipeSummary
    fun getRequiredCycles(): Double
    fun getOverclockingProfile(): Double
}

interface ExtractingSiteTaskSummary : TaskSummary {
    fun getExtractor(): ExtractorEntity
}

interface PowerGeneratingSiteTaskSummary : TaskSummary {
    fun getPowerGenerator(): PowerGeneratorSummary
    fun getPowerProducedInMegaWatt(): Double
}
