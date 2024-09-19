package terapps.factoryplanner.api.json.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.http.Method
import terapps.factoryplanner.core.dto.BucketEntryDto
import java.io.IOException
import java.util.concurrent.TimeUnit

class BucketEntrySerializer(
        private val minioClient: MinioClient,
        private val delegate: JsonSerializer<Any>?
) : JsonSerializer<BucketEntryDto>() {
    @Throws(IOException::class)
    override fun serialize(
            value: BucketEntryDto,
            gen: JsonGenerator,
            serializers: SerializerProvider) {
        delegate?.serialize(
                value.apply {
                    link = minioClient.getPresignedObjectUrl(
                            GetPresignedObjectUrlArgs.builder()
                                    .method(Method.GET)
                                    .`object`(value.objectPath)
                                    .expiry(2, TimeUnit.DAYS)
                                    .build()
                    )
                }, gen, serializers
        )
    }
}

