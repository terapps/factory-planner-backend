package terapps.factoryplanner.api.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import terapps.factoryplanner.core.entities.Extractor
import terapps.factoryplanner.core.entities.ItemCategory
import terapps.factoryplanner.filestorage.dto.AssetsBucketEntry

data class ItemDescriptorDto(
        val className: String,
        val displayName: String,
        val form: String,
        @JsonIgnore
        val iconPath: String,
        val sinkablePoints: Int?,
        val category: ItemCategory,
        val extractedIn: Set<Extractor>,
) {
    val icon: AssetsBucketEntry
        get() = AssetsBucketEntry(iconPath)
}