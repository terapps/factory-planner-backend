package terapps.factoryplanner.api.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import terapps.factoryplanner.core.dto.TaskDto
import terapps.factoryplanner.core.dto.TaskGroupDto
import terapps.factoryplanner.core.services.TaskService
import terapps.factoryplanner.core.services.components.request.TaskCreationDto
import terapps.factoryplanner.core.services.components.request.TaskIOCreationRequest


@RestController
@RequestMapping("/tasks")
class TaskController {
    @Autowired
    private lateinit var taskService: TaskService

    @GetMapping("/search", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchGroupByDisplayNameLike(
            @RequestParam("name") name: String,
    ): Collection<TaskGroupDto> {
        return taskService.searchGroupByName(name)
    }

    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getGroupById(
            @PathVariable("id") id: String,
    ): TaskGroupDto {
        return taskService.findGroupById(id)
    }

    @PutMapping("/{groupId}/bind", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun bindTask(
            @PathVariable("groupId") groupId: String,
            @RequestBody edges: Collection<TaskIOCreationRequest>,
    ) {
        return taskService.createTaskIo(*edges.toTypedArray())
    }

    @GetMapping("/{groupId}/task/{taskId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getTask(
            @PathVariable("groupId") groupId: String,
            @PathVariable("taskId") taskId: String,
    ): TaskDto {
        return taskService.findTaskById(taskId)
    }

    @PostMapping("/{groupId}/task", produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun saveTask(
            @PathVariable("groupId") id: String,
            @RequestBody taskDto: TaskCreationDto,
    ): TaskDto {
        return taskService.saveTask(id, taskDto)
    }

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun saveGroup(
            @RequestBody taskDto: TaskGroupDto,
    ): TaskGroupDto {
        return taskService.saveGroup(taskDto)
    }
}