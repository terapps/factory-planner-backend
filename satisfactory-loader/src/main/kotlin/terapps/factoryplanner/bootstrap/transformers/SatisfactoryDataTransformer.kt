package terapps.factoryplanner.bootstrap.transformers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.GameObjectCategory
import terapps.factoryplanner.bootstrap.steps.StepManager
import kotlin.reflect.KClass


@Component
class SatisfactoryDataTransformer {
    @Autowired
    private lateinit var stepManager: StepManager

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    private val transformers:   Collection<SatisfactoryTransformer<*, *>>
        get() {
            return applicationContext.getBeansOfType(SatisfactoryTransformer::class.java).values
        }


    private fun resolveTransformerClass(clazz: KClass<*>): SatisfactoryTransformer<Any, Any>? = transformers.find { it.supportsClass(clazz) } as SatisfactoryTransformer<Any, Any>?

    private fun transform(gameEntity: Any, transformer: SatisfactoryTransformer<Any, Any>) {
        transformer.run {
            save(transform(gameEntity))
        }
    }

    fun transformCategory(gameObjectCategory: GameObjectCategory<*>, onload: (category: GameObjectCategory<*>) -> Unit = {}) {
        val resolvedTransformer = resolveTransformerClass(gameObjectCategory.classType)

        resolvedTransformer?.let { transformer ->
            stepManager.prepareStep(gameObjectCategory, transformer)

            onload(gameObjectCategory)
            gameObjectCategory.Classes.forEachIndexed { index, gameObject ->
                println("Loading class ${gameObject.javaClass.simpleName} with transformer $${transformer.javaClass.simpleName}: ${index + 1} / ${gameObjectCategory.Classes.size}")
                transform(gameObject, transformer)
            }

            stepManager.disposeStep(gameObjectCategory, transformer)
        }
    }
}