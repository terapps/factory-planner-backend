package terapps.factoryplanner.core.projections

import terapps.factoryplanner.core.entities.Extractor
import terapps.factoryplanner.core.entities.ItemCategory


interface ItemDescriptorSummary {
    fun getClassName(): String
    fun getDisplayName(): String

   fun getForm(): String?

    fun getIconSmall(): String?

    fun getSinkablePoints(): Int?

    fun getCategory(): ItemCategory

    fun getExtractedIn(): Set<Extractor>
}
