package terapps.factoryplanner.core.services

import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import terapps.factoryplanner.core.dto.*
import terapps.factoryplanner.core.entities.*
import terapps.factoryplanner.core.projections.ItemDescriptorSummary
import terapps.factoryplanner.core.projections.RecipeProducingSummary
import terapps.factoryplanner.core.repositories.*
import terapps.factoryplanner.core.services.components.CraftingSiteTaskCreationDto
import terapps.factoryplanner.core.services.components.ExtractingSiteTaskCreationDto
import terapps.factoryplanner.core.services.components.PowerGeneratingSiteTaskCreationDto
import terapps.factoryplanner.core.services.components.TaskCreationDto
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
class TaskService {
    @Autowired
    private lateinit var taskRepository: TaskRepository

    @Autowired
    private lateinit var taskGroupRepository: TaskGroupRepository

    @Autowired
    private lateinit var recipeRepository: RecipeRepository

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
        val entity = taskGroupRepository.findById(id).getOrElse { throw Error("Task group not exist") }


        return modelMapper.map(entity, TaskGroupDto::class.java)
    }

    fun findTaskById(id: String): TaskDto {
        val entity = taskRepository.findById(id).getOrElse { throw Error("Task not exist") }

        return modelMapper.map(entity, TaskDto::class.java)
    }
    fun bindTasks(
            taskEdge: Pair<String, String>
    ) {
        val source = taskRepository.findById(taskEdge.first).getOrElse { throw Error("Source does not exist") }
        val destination = taskRepository.findById(taskEdge.second).getOrElse { throw Error("Destination does not exist") }

        source.outgoing += destination

        taskRepository.save(source)
    }

    fun saveTask(groupId: String, taskDto: TaskCreationDto): TaskDto {
        val taskGroup = taskGroupRepository.findById(groupId).getOrElse { throw Error("Unknown group") }
        val task = saveTask(taskDto)

        taskGroupRepository.save(TaskGroupEntity(taskGroup.name).apply {
            id = taskGroup.id
            name = taskGroup.name
            tasks = setOf(task).plus(taskGroup.tasks)
        })
        return modelMapper.map(task, TaskDto::class.java)
    }

    fun saveGroup(taskGroupDto: TaskGroupDto): TaskGroupDto {
        val entity = taskGroupRepository.save(
                TaskGroupEntity(taskGroupDto.name)
        )
        println(entity)

        return modelMapper.map(entity, TaskGroupDto::class.java)
    }

    private fun saveTask(taskDto: TaskCreationDto): TaskEntity = when (taskDto) {
        is CraftingSiteTaskCreationDto -> saveCraftingSite(taskDto)
        is ExtractingSiteTaskCreationDto -> saveExtractingSite(taskDto)
        is PowerGeneratingSiteTaskCreationDto -> savePowerGeneratingSiteEntity(taskDto)
        else -> throw Error("Unknown task")
    }.apply {
        status = taskDto.status
    }

    private fun saveCraftingSite(taskDto: CraftingSiteTaskCreationDto): CraftingSiteTaskEntity {
        val recipe = recipeRepository.findByClassName(taskDto.recipe, RecipeProducingSummary::class.java) ?: throw Error("Unknown recipe")

        return taskRepository.save(CraftingSiteTaskEntity(
                taskDto.requiredCycles,
                recipe,
                taskDto.overclockingProfile
        ))
    }

    private fun saveExtractingSite(taskDto: ExtractingSiteTaskCreationDto): ExtractingSiteTaskEntity {
        val extractor = extractorRepository.findByClassName(taskDto.extractor) ?: throw Error("Unknown extractor")
        val item = itemDescriptorRepository.findByClassName(taskDto.item, ItemDescriptorSummary::class.java) ?: throw Error("Unkown item")

        return taskRepository.save(ExtractingSiteTaskEntity(
                taskDto.nodes,
                item,
                extractor
        ))
    }

    private fun savePowerGeneratingSiteEntity(taskDto: PowerGeneratingSiteTaskCreationDto): PowerGeneratingTaskEntity {
        val powerGenerator = powerGeneratorRepository.findByClassName(taskDto.powerGeneratorDto) ?: throw Error("Unknown power generator")
        val fuel = itemDescriptorRepository.findByClassName(taskDto.selectedFuel, ItemDescriptorSummary::class.java) ?: throw Error("Unkown item")

        return taskRepository.save(PowerGeneratingTaskEntity(
                fuel,
                powerGenerator,
                taskDto.powerProducedInMegaWatt
        ))
    }

}