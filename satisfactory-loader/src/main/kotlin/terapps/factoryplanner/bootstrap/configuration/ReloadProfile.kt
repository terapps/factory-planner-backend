package terapps.factoryplanner.bootstrap.configuration

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties("satisfactory.planner.reload-profile")
class ReloadProfile(
        val wipe: Boolean,
        val steps: Set<String>
)