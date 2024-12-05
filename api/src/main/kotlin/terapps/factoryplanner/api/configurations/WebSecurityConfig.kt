package terapps.factoryplanner.api.configurations

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebSecurityConfig : WebMvcConfigurer {
    @Override
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
                .allowedOrigins("http://192.168.1.61:4200", "http://localhost:9876")
                .allowedMethods("GET", "OPTIONS", "POST")
    }

}