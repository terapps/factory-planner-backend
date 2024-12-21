package terapps.factoryplanner.core.services

import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import terapps.factoryplanner.core.dto.ItemDescriptorDto
import terapps.factoryplanner.core.dto.PowerGeneratorDto
import terapps.factoryplanner.core.projections.ItemDescriptorSummary
import terapps.factoryplanner.core.repositories.PowerGeneratorRepository

@Service
class PowerGeneratorService {
    @Autowired
    private lateinit var powerGeneratorRepository: PowerGeneratorRepository

    @Autowired
    private lateinit var modelMapper: ModelMapper

    fun findByClassName(className: String): PowerGeneratorDto {
        val item = powerGeneratorRepository.findByClassName(className)

        return modelMapper.map(item, PowerGeneratorDto::class.java)
    }

    fun findByDisplayNameLike(displayName: String): Collection<PowerGeneratorDto> {
        val items = powerGeneratorRepository.findByDisplayNameLikeIgnoreCase(displayName)

        return items.map {
            modelMapper.map(it, PowerGeneratorDto::class.java)
        }
    }

    fun findAll(): Collection<PowerGeneratorDto> {
        val items = powerGeneratorRepository.findAll()

        return items.map { modelMapper.map(it, PowerGeneratorDto::class.java) }
    }
}