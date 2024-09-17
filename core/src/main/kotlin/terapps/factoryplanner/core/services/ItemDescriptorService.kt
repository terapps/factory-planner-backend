package terapps.factoryplanner.core.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import terapps.factoryplanner.core.projections.ItemDescriptorSummary
import terapps.factoryplanner.core.repositories.ItemDescriptorRepository

@Service
class ItemDescriptorService {
    @Autowired
    private lateinit var itemDescriptorRepository: ItemDescriptorRepository

    fun findByClassName(className: String): ItemDescriptorSummary = itemDescriptorRepository.findByClassName(className, ItemDescriptorSummary::class.java)?: throw Error("Cannot find ItemDescriptor from classname: $className")
}