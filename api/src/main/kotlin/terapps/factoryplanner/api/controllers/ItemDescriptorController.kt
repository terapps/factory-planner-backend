package terapps.factoryplanner.api.controllers

import io.minio.MinioClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import terapps.factoryplanner.core.dto.ItemDescriptorDto
import terapps.factoryplanner.core.projections.ItemDescriptorSummary
import terapps.factoryplanner.core.services.ItemDescriptorService

@RestController
@RequestMapping("/item-descriptors")
class ItemDescriptorController {
    @Autowired
    private lateinit var itemDescriptorService: ItemDescriptorService


    @GetMapping("/{itemClassName}")
    fun findByClassName(
            @PathVariable("itemClassName") itemClassName: String,
    ): ItemDescriptorDto {
        return itemDescriptorService.findByClassName(itemClassName)
    }

    @GetMapping
    fun searchByDisplayNameLike(
            @RequestParam("displayName") itemClass: String,
    ): Collection<ItemDescriptorDto> {
        return itemDescriptorService.findByDisplayNameLike(itemClass)
    }

}