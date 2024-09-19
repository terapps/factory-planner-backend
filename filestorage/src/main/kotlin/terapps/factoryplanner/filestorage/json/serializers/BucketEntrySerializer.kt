package terapps.factoryplanner.filestorage.json.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import terapps.factoryplanner.filestorage.dto.BucketEntry
import terapps.factoryplanner.filestorage.services.FileStorageService
import java.io.IOException

class BucketEntrySerializer(
        private val fileStorageService: FileStorageService,
        private val delegate: JsonSerializer<Any>?
) : JsonSerializer<BucketEntry>() {
    @Throws(IOException::class)
    override fun serialize(
            value: BucketEntry,
            gen: JsonGenerator,
            serializers: SerializerProvider) {
        delegate?.serialize(
                value.apply {
                    link = fileStorageService.getSignedUrl(
                            value.bucket,
                            value.objectPath,
                    )
                }, gen, serializers
        )
    }
}

