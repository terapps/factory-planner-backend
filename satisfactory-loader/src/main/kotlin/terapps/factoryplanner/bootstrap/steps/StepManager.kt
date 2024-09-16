package terapps.factoryplanner.bootstrap.steps

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service

@Service
class StepManager {
    @Autowired
    private lateinit var applicationContext: ApplicationContext

    private val rootSteps: Collection<RootStep>
        get() {
            return applicationContext.getBeansOfType(RootStep::class.java).values
        }

    fun prepare() {
        rootSteps.forEach {
            it.prepare()
        }
    }

    fun dispose() {
        rootSteps.forEach {
            it.dispose()
        }
    }
}