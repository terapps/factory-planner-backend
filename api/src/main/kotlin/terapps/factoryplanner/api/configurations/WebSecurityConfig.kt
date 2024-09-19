package terapps.factoryplanner.api.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import java.util.*

@EnableWebSecurity
@Configuration
class WebSecurityConfig {
    @Bean
    fun  filterChain(http: HttpSecurity): SecurityFilterChain {
        return http.cors {
            it.configurationSource {
                CorsConfiguration().apply {
                    allowedOrigins = Arrays.asList("*") // TODO if dev
                }
            }
        }.build()
    }
}