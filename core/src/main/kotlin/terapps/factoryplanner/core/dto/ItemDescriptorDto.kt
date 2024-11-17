package terapps.factoryplanner.core.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import terapps.factoryplanner.core.entities.ExtractorEntity
import terapps.factoryplanner.core.entities.ItemCategory
import terapps.factoryplanner.core.entities.ItemDescriptorEntity
import terapps.factoryplanner.core.projections.ItemDescriptorSummary
import terapps.factoryplanner.filestorage.dto.AssetsBucketEntry

class ItemDescriptorDto(
        val className: String,
        val displayName: String,
        val form: String,
        @JsonIgnore
        val iconPath: String,
        val sinkablePoints: Int?,
        val category: ItemCategory,
        val extractedIn: Set<ExtractorDto>,
) {
    val icon: AssetsBucketEntry
        get() = AssetsBucketEntry(iconPath)

    constructor(itemDescriptorSummary: ItemDescriptorSummary): this(
            itemDescriptorSummary.getClassName(),
            itemDescriptorSummary.getDisplayName(),
            itemDescriptorSummary.getForm()!!,
            itemDescriptorSummary.getIconSmall()!!,
            itemDescriptorSummary.getSinkablePoints(),
            itemDescriptorSummary.getCategory(),
            itemDescriptorSummary.getExtractedIn().map { ExtractorDto(it) }.toSet()
    )

    constructor(itemDescriptor: ItemDescriptorEntity): this(
            itemDescriptor.className,
            itemDescriptor.displayName!!,
            itemDescriptor.form!!,
            itemDescriptor.iconSmall!!,
            itemDescriptor.sinkablePoints,
            itemDescriptor.category!!,
            itemDescriptor.extractedIn.map { ExtractorDto(it) }.toSet()
    )
}