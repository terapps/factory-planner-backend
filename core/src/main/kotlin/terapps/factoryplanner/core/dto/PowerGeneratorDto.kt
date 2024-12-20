package terapps.factoryplanner.core.dto

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Relationship
import terapps.factoryplanner.core.entities.PowerGeneratorProduces
import terapps.factoryplanner.core.entities.PowerGeneratorRequires

class PowerGeneratorDto(
        @Id
        val className: String,
        val currentPotential: Double,
        val fuelLoadAmount: Double,
        val powerProduction: Double,
        val fuel: PowerGeneratorRequires,
        val supplementalResource: ItemIoDto? = null,
        val byproducts: ItemIoDto? = null,
)
