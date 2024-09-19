package terapps.factoryplanner.core.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import terapps.factoryplanner.core.dto.ItemDescriptorDto
import terapps.factoryplanner.core.projections.ItemDescriptorSummary
import terapps.factoryplanner.core.repositories.ItemDescriptorRepository

fun ItemDescriptorSummary.toDto() = ItemDescriptorDto(
        getClassName(),
        getDisplayName(),
        getForm()!!,
        getIconSmall()!!,
        getSinkablePoints(),
        getCategory(),
        getExtractedIn()
)

@Service
class ItemDescriptorService {
    @Autowired
    private lateinit var itemDescriptorRepository: ItemDescriptorRepository

    fun findByClassName(className: String): ItemDescriptorDto {
        val item = itemDescriptorRepository.findByClassName(className, ItemDescriptorSummary::class.java)

        return item?.toDto() ?: throw Error("Cannot find ItemDescriptor from classname: $className")
    }

    fun findByDisplayNameLike(displayName: String): Collection<ItemDescriptorDto> {
        val items = itemDescriptorRepository.findByDisplayNameLikeIgnoreCase(displayName, ItemDescriptorSummary::class.java)

        return items.map { it.toDto() }
    }
}