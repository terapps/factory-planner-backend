package terapps.factoryplanner.bootstrap.steps

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import kotlin.math.log

@Service
class StepManager {
    @Autowired
    private lateinit var applicationContext: ApplicationContext

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val rootSteps: Collection<RootStep>
        get() {
            return applicationContext.getBeansOfType(RootStep::class.java).values.sortedBy { it.priority }
        }

    fun prepare() {
        rootSteps.forEach {
            logger.info("Running prepare step ${it.javaClass.simpleName}")
            it.prepare()
        }
    }

    fun dispose() {
        rootSteps.forEach {
            logger.info("Running dispose step ${it.javaClass.simpleName}")
            it.dispose()
        }
    }
}