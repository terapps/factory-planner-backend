package terapps.factoryplanner.core.dto

import terapps.factoryplanner.core.entities.Extractor
import terapps.factoryplanner.core.entities.ItemCategory

data class ItemDescriptorDto(
        val className: String,
        val displayName: String,
        val form: String,
        val iconPath: String,
        val sinkablePoints: Int?,
        val category: ItemCategory,
        val extractedIn: Set<Extractor>,
) {
    val bucketEntry: BucketEntryDto
        get() = BucketEntryDto(
                "assets",
                iconPath
        )
}