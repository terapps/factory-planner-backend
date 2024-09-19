package terapps.factoryplanner.filestorage.dto

import com.fasterxml.jackson.annotation.JsonIgnore

abstract class BucketEntry(
        @JsonIgnore
        open val bucket: BucketEnum,
        open val objectPath: String,
        open var link: String?
)

data class AssetsBucketEntry(
        override val objectPath: String,
        override var link: String? = null
): BucketEntry(BucketEnum.Assets, objectPath, link)

data class ResourcesBucketEntry(
        override val objectPath: String,
        override var link: String? = null
): BucketEntry(BucketEnum.Assets, objectPath, link)