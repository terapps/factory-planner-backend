package terapps.factoryplanner.bootstrap.steps

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Service
import terapps.factoryplanner.bootstrap.dto.GameObjectCategory
import terapps.factoryplanner.bootstrap.transformers.SatisfactoryTransformer
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

@Service
class StepManager {
    @Autowired
    private lateinit var applicationContext: ApplicationContext

    private val steps: Collection<Step<Any>>
        get() {
            return applicationContext.getBeansOfType(Step::class.java).values as Collection<Step<Any>>
        }
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

    fun prepareStep(category: GameObjectCategory<*>, transformer: SatisfactoryTransformer<Any, Any>) {
        resolveStep(category.javaClass.kotlin)?.run {
            prepare(category as GameObjectCategory<Any>)
        }
    }

    fun disposeStep(category: GameObjectCategory<*>, transformer: SatisfactoryTransformer<Any, Any>) {
        resolveStep(transformer.javaClass.kotlin)?.run {
            dispose(category as GameObjectCategory<Any>)
        }
    }

    private fun resolveStep(clazz: KClass<*>): Step<Any>? {
        return steps.find { it.javaClass.kotlin.isSubclassOf(clazz) }
    }
}