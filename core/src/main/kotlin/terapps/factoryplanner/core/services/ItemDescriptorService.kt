package terapps.factoryplanner.core.services

import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import terapps.factoryplanner.core.dto.ExtractorDto
import terapps.factoryplanner.core.dto.ItemDescriptorDto
import terapps.factoryplanner.core.entities.ItemCategory
import terapps.factoryplanner.core.entities.ItemDescriptorEntity
import terapps.factoryplanner.core.projections.ItemDescriptorSummary
import terapps.factoryplanner.core.repositories.ItemDescriptorRepository

fun ItemDescriptorSummary.toDto() = ItemDescriptorDto(
        getClassName(),
        getDisplayName(),
        getForm()!!,
        getIconSmall()!!,
        getSinkablePoints(),
        getCategory(),
        getExtractedIn().map { ExtractorDto(it) }.toSet()
)

@Service
class ItemDescriptorService {
    @Autowired
    private lateinit var itemDescriptorRepository: ItemDescriptorRepository

    @Autowired
    private lateinit var modelMapper: ModelMapper

    fun findByClassName(className: String): ItemDescriptorDto {
        val item = itemDescriptorRepository.findByClassName(className, ItemDescriptorSummary::class.java)

        return modelMapper.map(item, ItemDescriptorDto::class.java)
    }

    fun findByCategory(category: ItemCategory): ItemDescriptorDto {
        val item = itemDescriptorRepository.findByCategory(category, ItemDescriptorSummary::class.java)

        return modelMapper.map(item, ItemDescriptorDto::class.java)
    }

    fun findByDisplayNameLike(displayName: String): Collection<ItemDescriptorDto> {
        val items = itemDescriptorRepository.findByDisplayNameLikeIgnoreCase(displayName, ItemDescriptorSummary::class.java)

        return items.map {
            modelMapper.map(it, ItemDescriptorDto::class.java)
        }
    }
}