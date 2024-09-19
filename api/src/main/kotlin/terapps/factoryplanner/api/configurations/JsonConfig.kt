package terapps.factoryplanner.api.configurations

import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.minio.MinioClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import terapps.factoryplanner.api.json.serializers.BucketEntrySerializer
import terapps.factoryplanner.core.dto.BucketEntryDto

@Configuration
class JsonConfig {
    @Bean
    fun registerObjectMapper(minioClient: MinioClient): ObjectMapper = ObjectMapper().apply {
        registerModules(
                KotlinModule.Builder().build(),
                SimpleModule().apply {
                    setSerializerModifier(object : BeanSerializerModifier() {
                        override fun modifySerializer(
                                config: SerializationConfig?, beanDesc: BeanDescription, serializer: JsonSerializer<*>?): JsonSerializer<*>? {
                            return if (beanDesc.beanClass.equals(BucketEntryDto::class.java)) {
                                BucketEntrySerializer(minioClient, serializer as JsonSerializer<Any>?)
                            } else serializer
                        }
                    })
                }
        )
    }

}