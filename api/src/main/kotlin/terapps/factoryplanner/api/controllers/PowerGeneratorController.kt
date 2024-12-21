package terapps.factoryplanner.api.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import terapps.factoryplanner.core.dto.ItemDescriptorDto
import terapps.factoryplanner.core.dto.PowerGeneratorDto
import terapps.factoryplanner.core.services.ItemDescriptorService
import terapps.factoryplanner.core.services.PowerGeneratorService

@RestController
@RequestMapping("/power-generator")
class PowerGeneratorController {
    @Autowired
    private lateinit var powerGeneratorService: PowerGeneratorService

    @GetMapping("/{className}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findByClassName(
            @PathVariable("className") className: String,
    ): PowerGeneratorDto {
        return powerGeneratorService.findByClassName(className)
    }

    @GetMapping("/search", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchByDisplayNameLike(
            @RequestParam("displayName") itemClass: String,
    ): Collection<PowerGeneratorDto> {
        return powerGeneratorService.findByDisplayNameLike(itemClass)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findAll(
    ): Collection<PowerGeneratorDto> {
        return powerGeneratorService.findAll()
    }

}