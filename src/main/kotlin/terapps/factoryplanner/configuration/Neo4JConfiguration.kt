package terapps.factoryplanner.configuration

import org.neo4j.cypherdsl.core.renderer.Configuration
import org.neo4j.cypherdsl.core.renderer.Dialect
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component


@Component
class Neo4JConfiguration {
    @Bean
    fun cypherDslConfiguration(): Configuration {
        return Configuration.newConfig()
                .withDialect(Dialect.NEO4J_5).build()
    }
}