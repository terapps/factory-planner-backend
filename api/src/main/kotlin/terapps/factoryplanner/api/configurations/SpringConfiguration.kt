package terapps.factoryplanner.api.configurations

import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.SpringHandlerInstantiator

@Configuration
class SpringConfiguration {

    @Bean
    fun handlerInstantiator(beanFactory: AutowireCapableBeanFactory): SpringHandlerInstantiator {
        return SpringHandlerInstantiator(beanFactory)
    }
}