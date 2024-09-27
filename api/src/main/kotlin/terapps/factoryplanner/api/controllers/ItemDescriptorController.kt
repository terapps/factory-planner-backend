package terapps.factoryplanner.api.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import terapps.factoryplanner.core.dto.ItemDescriptorDto
import terapps.factoryplanner.core.services.ItemDescriptorService

@RestController
@RequestMapping("/item-descriptors")
class ItemDescriptorController {
    @Autowired
    private lateinit var itemDescriptorService: ItemDescriptorService


    @GetMapping("/{itemClassName}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun findByClassName(
            @PathVariable("itemClassName") itemClassName: String,
    ): ItemDescriptorDto {
        return itemDescriptorService.findByClassName(itemClassName)
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun searchByDisplayNameLike(
            @RequestParam("displayName") itemClass: String,
    ): Collection<ItemDescriptorDto> {
        return itemDescriptorService.findByDisplayNameLike(itemClass)
    }

}