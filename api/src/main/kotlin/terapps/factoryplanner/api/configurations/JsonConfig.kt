package terapps.factoryplanner.api.configurations

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import terapps.factoryplanner.filestorage.json.FileStorageModule
import terapps.factoryplanner.filestorage.services.FileStorageService

@Configuration
class JsonConfig {
    @Bean
    fun registerObjectMapper(fileStorageService: FileStorageService): ObjectMapper = ObjectMapper().apply {
        registerModules(
                KotlinModule.Builder().build(),
                FileStorageModule(fileStorageService)
        )
    }

}