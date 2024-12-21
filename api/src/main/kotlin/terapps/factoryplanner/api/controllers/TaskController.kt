package terapps.factoryplanner.api.controllers

import jakarta.websocket.server.PathParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import terapps.factoryplanner.core.dto.PowerGeneratorDto
import terapps.factoryplanner.core.dto.TaskDto
import terapps.factoryplanner.core.dto.TaskGroupDto
import terapps.factoryplanner.core.services.TaskService
import terapps.factoryplanner.core.services.components.TaskCreationDto
import java.util.UUID

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

    @GetMapping("/:id", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchByDisplayNameLike(
            @PathParam("id") id: String,
    ): TaskGroupDto {
        return taskService.findGroupById(id)
    }

    @PutMapping("/:sourceId/bind/:destinationId", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchByDisplayNameLike(
            @PathParam("sourceId") sourceId: String,
            @PathParam("destinationId") destId: String,
    ) {
        return taskService.bindTasks(sourceId to destId)
    }

    @GetMapping("/:groupId/task/:taskId", produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun getTask(
            @PathParam("groupId") groupId: String,
            @PathParam("taskId") taskId: String,
    ): TaskDto {
        return taskService.findTaskById(taskId)
    }

    @PostMapping("/:groupId/task", produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun saveTask(
            @PathParam("groupId") id: String,
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