package terapps.factoryplanner.core.dto

import org.springframework.data.neo4j.core.schema.Id
import terapps.factoryplanner.core.entities.Automaton
import terapps.factoryplanner.core.entities.CraftingMachineEntity
import terapps.factoryplanner.core.entities.ExtractorEntity
import terapps.factoryplanner.filestorage.dto.AssetsBucketEntry

class ExtractorDto(
        override val className: String,
        override val displayName: String,
        val description: String,
        val extractCycleTime: Double,
        val itemsPerCycle: Int,
        val powerConsumption: Double,
        val powerConsumptionExponent: Double,
        override val minPotential: Double,
        override val maxPotential: Double,
        override val productionBoost: Double,
        val extractorType: String, // TODO types enum
): Automaton {
    constructor(it: ExtractorEntity): this(
            it.className,
            it.displayName,
            it.description,
            it.extractCycleTime,
            it.itemsPerCycle,
            it.powerConsumption,
            it.powerConsumptionExponent,
            it.minPotential,
            it.maxPotential,
            it.productionBoost,
            it.extractorType,
    )

}