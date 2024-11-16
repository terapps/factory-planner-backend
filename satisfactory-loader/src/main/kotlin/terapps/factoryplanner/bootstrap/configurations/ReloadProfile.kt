package terapps.factoryplanner.bootstrap.configurations

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties("satisfactory.planner.reload-profile")
class ReloadProfile(
        val wipe: Boolean,
        val steps: Collection<String>
)