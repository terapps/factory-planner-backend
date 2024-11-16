package terapps.factoryplanner.bootstrap.steps

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import terapps.factoryplanner.bootstrap.configurations.ReloadProfile

@Service
class StepManager {
    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Autowired
    private lateinit var reloadProfile: ReloadProfile

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val rootSteps: Collection<RootStep>
        get() {
            return applicationContext.getBeansOfType(RootStep::class.java).values.sortedBy { it.priority }
        }

    private fun Collection<RootStep>.includedStep() = filter { step ->
        val selectedSteps = reloadProfile.steps
        val stepClass = step.javaClass.name

        if (selectedSteps.isEmpty()) true else selectedSteps.any {
            stepClass.contains(it)
        }
    }

    fun prepare() {
        rootSteps.includedStep().forEach {
            logger.info("Running prepare step ${it.javaClass.simpleName}")
            it.prepare()
        }
    }

    fun dispose() {
        rootSteps.includedStep().forEach {
            logger.info("Running dispose step ${it.javaClass.simpleName}")
            it.dispose()
        }
    }
}