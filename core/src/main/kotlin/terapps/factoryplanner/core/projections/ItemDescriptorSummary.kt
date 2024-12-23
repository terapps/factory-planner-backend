package terapps.factoryplanner.core.projections

import terapps.factoryplanner.core.entities.ExtractorEntity
import terapps.factoryplanner.core.entities.ItemCategory

interface ItemDescriptorMetadata {
    fun getClassName(): String
    fun getDisplayName(): String
    fun getForm(): String?
    fun getIconSmall(): String?
    fun getSinkablePoints(): Int?
    fun getCategory(): ItemCategory
}

interface ItemDescriptorSummary {
    fun getClassName(): String
    fun getDisplayName(): String

   fun getForm(): String?

    fun getIconSmall(): String?

    fun getSinkablePoints(): Int?

    fun getCategory(): ItemCategory

    fun getExtractedIn(): Set<ExtractorEntity>
}
