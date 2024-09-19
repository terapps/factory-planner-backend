package terapps.factoryplanner.core.dto

data class BucketEntryDto(
        val bucket: String,
        val objectPath: String,
        var link: String? = null
) { // TODO somewhere else

}