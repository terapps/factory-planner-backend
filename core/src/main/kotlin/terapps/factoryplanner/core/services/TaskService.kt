package terapps.factoryplanner.core.services

import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.core.Neo4jClient
import org.springframework.stereotype.Service
import terapps.factoryplanner.core.dto.*
import terapps.factoryplanner.core.entities.*
import terapps.factoryplanner.core.projections.*
import terapps.factoryplanner.core.repositories.*
import terapps.factoryplanner.core.services.components.Relationship
import terapps.factoryplanner.core.services.components.relation.*
import terapps.factoryplanner.core.services.components.request.*
import kotlin.jvm.optionals.getOrNull


@Service
class TaskService {
    @Autowired
    private lateinit var taskRepository: TaskRepository

    @Autowired
    private lateinit var taskIORepository: TaskIORepository

    @Autowired
    private lateinit var taskGroupRepository: TaskGroupRepository

    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    @Autowired
    private lateinit var neo4jClient: Neo4jClient

    @Autowired
    private lateinit var extractorRepository: ExtractorRepository

    @Autowired
    private lateinit var itemDescriptorRepository: ItemDescriptorRepository

    @Autowired
    private lateinit var powerGeneratorRepository: PowerGeneratorRepository

    @Autowired
    private lateinit var modelMapper: ModelMapper

    fun searchGroupByName(name: String): Collection<TaskGroupDto> {
        val entities = taskGroupRepository.findByNameLikeIgnoreCase(name)

        return entities.map {
            modelMapper.map(it, TaskGroupDto::class.java)
        }
    }

    fun findGroupById(id: String): TaskGroupDto {
        val entity = taskGroupRepository.findById(id, TaskGroupSummary::class.java) ?: throw Error("Task group not exist")

        return modelMapper.map(entity, TaskGroupDto::class.java)
    }

    fun findTaskById(id: String): TaskDto {
        val taskEntity = taskRepository.findById(id, TaskMinimalSummary::class.java) ?: throw Error("Task not exist")
        val projectionType = taskEntity.getType().getTaskToProjection()
        val entity = taskRepository.findById(id, projectionType.java) ?: throw Error("Task not exist")
        val taskClass = entity.getType().getTaskDtoClass()

        return modelMapper.map(entity, taskClass.java)
    }

    fun createTaskIo(
            vararg taskEdge: TaskIOCreationRequest
    ) {
        val itemToTaskIo = ItemToTaskIo(neo4jClient)
        val ioToTask = IoToTask(neo4jClient)
        val taskToIo = TaskToIo(neo4jClient)

        taskEdge.forEach {
            if (!taskRepository.existsById(it.source)) {
                throw Error("Source does not exist ${it.source}")
            }
            if (!taskRepository.existsById(it.destination)) {
                throw Error("Destination does not exist ${it.source}")
            }
            if (!itemDescriptorRepository.existsByClassName(it.item)) {
                throw Error("Item does not exist ${it.item}")
            }

            val createdIo = taskIORepository.save(TaskIoEntity(it.amountPerMinute))

            itemToTaskIo.relationships += Relationship(it.item, createdIo.id)
            taskToIo.relationships += Relationship(it.source, createdIo.id)
            ioToTask.relationships += Relationship(createdIo.id, it.destination)
        }
        itemToTaskIo.runCypherQuery()
        taskToIo.runCypherQuery()
        ioToTask.runCypherQuery()
    }

    fun saveTask(taskGroupId: String, taskDto: TaskCreationDto): TaskDto {
        val taskGroupExists = taskGroupRepository.existsById(taskGroupId)

        if (!taskGroupExists) throw Error("Group does not exist")

        val task = saveTask(taskDto)
        val taskClass = task.getType().getTaskDtoClass()
        val groupRelation = TaskToGroup(neo4jClient, listOf(Relationship(task.getId(), taskGroupId)))

        groupRelation.runCypherQuery()

        return modelMapper.map(task, taskClass.java)
    }

    fun saveGroup(taskGroupDto: TaskGroupDto): TaskGroupDto {
        val entity = taskGroupRepository.save(
                TaskGroupEntity(taskGroupDto.name)
        )

        return modelMapper.map(entity, TaskGroupDto::class.java)
    }

    private fun saveTask(taskDto: TaskCreationDto): TaskSummary = when (taskDto) {
        is CraftingSiteTaskCreationDto -> saveCraftingSite(taskDto)
        is ExtractingSiteTaskCreationDto -> saveExtractingSite(taskDto)
        is PowerGeneratingSiteTaskCreationDto -> savePowerGeneratingSiteEntity(taskDto)
        else -> throw Error("Unknown task")
    }

    private fun saveCraftingSite(taskDto: CraftingSiteTaskCreationDto): TaskSummary {
        val recipeExisting = recipeRepository.existsByClassName(taskDto.recipe)

        if (!recipeExisting) {
            throw Error("Recipe ${taskDto.recipe} does not exits")
        }

        val createdTask = taskRepository.save(TaskEntity().apply {
            status = taskDto.status
            type = taskDto.type
            requiredCycles = taskDto.requiredCycles
            overclockingProfile = taskDto.overclockingProfile
        })

        val recipeRelation = RecipeToTask(neo4jClient, listOf(Relationship(taskDto.recipe, createdTask.id)))

        recipeRelation.runCypherQuery()

        val projectionType = taskDto.type.getTaskToProjection()

        return taskRepository.findById(createdTask.id, projectionType.java) ?: throw Error("Task failed to be created")
    }

    private fun saveExtractingSite(taskDto: ExtractingSiteTaskCreationDto): TaskSummary {
        val existingExtractor = extractorRepository.existsByClassName(taskDto.extractor)

        if (!existingExtractor) {
            throw Error("Extractor ${taskDto.extractor} does not exist")
        }
        val createdTask = taskRepository.save(TaskEntity().apply {
            status = taskDto.status
            type = taskDto.type
        })
        val extractorRelation = ExtractorToTask(neo4jClient, listOf(Relationship(taskDto.extractor, createdTask.id)))

        extractorRelation.runCypherQuery()


        val projectionType = taskDto.type.getTaskToProjection()

        return taskRepository.findById(createdTask.id, projectionType.java) ?: throw Error("Task failed to be created")
    }

    private fun savePowerGeneratingSiteEntity(taskDto: PowerGeneratingSiteTaskCreationDto): TaskSummary {
        val powerGeneratorExists = powerGeneratorRepository.existsByClassName(taskDto.powerGenerator)

        if (!powerGeneratorExists) {
            throw Error("Generator ${taskDto.powerGenerator} does not exist")
        }
        val createdTask = taskRepository.save(TaskEntity().apply {
            powerProducedInMegaWatt = taskDto.powerProducedInMegaWatt
            status = taskDto.status
            type = taskDto.type
        })

        val powerRelation = PowerGeneratorToTask(neo4jClient, listOf(Relationship(taskDto.powerGenerator, createdTask.id)))

        powerRelation.runCypherQuery()

        val projectionType = taskDto.type.getTaskToProjection()

        return taskRepository.findById(createdTask.id, projectionType.java) ?: throw Error("Task failed to be created")
    }

    private fun TaskEnum.getTaskToProjection() = when (this) {
        TaskEnum.CraftingSite -> CraftingSiteTaskSummary::class
        TaskEnum.ExtractingSite -> ExtractingSiteTaskSummary::class
        TaskEnum.PowerGeneratingSite -> PowerGeneratingSiteTaskSummary::class
    }

    private fun TaskEnum.getTaskDtoClass() = when (this) {
        TaskEnum.CraftingSite -> CraftingSiteTaskDto::class
        TaskEnum.ExtractingSite -> ExtractingSiteTaskDto::class
        TaskEnum.PowerGeneratingSite -> PowerGeneratingSiteTaskDto::class
    }
}