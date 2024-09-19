package terapps.factoryplanner.configurations

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.http.Method
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
                                    .bucket(value.bucket)
                                    .`object`(value.objectPath)
                                    .expiry(2, TimeUnit.DAYS)
                                    .build()
                    )
                }, gen, serializers
        )
    }
}


class FolderBeanSerializerModifier(private val minioClient: MinioClient) : BeanSerializerModifier() {
    override fun modifySerializer(
            config: SerializationConfig?, beanDesc: BeanDescription, serializer: JsonSerializer<*>?): JsonSerializer<*>? {
        return if (beanDesc.beanClass.equals(BucketEntryDto::class.java)) {
            BucketEntrySerializer(minioClient, serializer as JsonSerializer<Any>?)
        } else serializer
    }
}

@Configuration
class JsonConfig {
    @Bean
    fun registerObjectMapper(minioClient: MinioClient): ObjectMapper = ObjectMapper().apply {
        registerModules(
                KotlinModule.Builder().build(),
                SimpleModule().apply {
                    setSerializerModifier(FolderBeanSerializerModifier(minioClient))
                }
        )
    }

}