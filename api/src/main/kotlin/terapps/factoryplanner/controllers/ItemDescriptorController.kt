package terapps.factoryplanner.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import terapps.factoryplanner.core.projections.ItemDescriptorSummary
import terapps.factoryplanner.core.projections.RecipeProducingSummary
import terapps.factoryplanner.core.projections.RecipeRequiringSummary
import terapps.factoryplanner.core.projections.RecipeSummary
import terapps.factoryplanner.core.services.ItemDescriptorService
import terapps.factoryplanner.core.services.RecipeService

@RestController
@RequestMapping("/item-descriptors")
class ItemDescriptorController {
    @Autowired
    private lateinit var itemDescriptorService: ItemDescriptorService

    @GetMapping("/{itemClassName}")
    fun findByClassNameProducing(
            @PathVariable("itemClassName") itemClassName: String,
    ): ItemDescriptorSummary {
        return itemDescriptorService.findByClassName(itemClassName)
    }
}