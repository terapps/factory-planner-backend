package terapps.factoryplanner.filestorage.json

import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
import terapps.factoryplanner.filestorage.dto.BucketEntry
import terapps.factoryplanner.filestorage.json.serializers.BucketEntrySerializer
import terapps.factoryplanner.filestorage.services.FileStorageService

class FileStorageModule(
        private val fileStorageService: FileStorageService
) : SimpleModule() {

    init {
        setSerializerModifier(object: BeanSerializerModifier() {
            override fun modifySerializer(
                    config: SerializationConfig?, beanDesc: BeanDescription, serializer: JsonSerializer<*>?): JsonSerializer<*>? {
                println(beanDesc.beanClass)
                return if (BucketEntry::class.java.isAssignableFrom(beanDesc.beanClass)) {
                    BucketEntrySerializer(fileStorageService, serializer as JsonSerializer<Any>?)
                } else serializer
            }
        })
    }
}