package terapps.factoryplanner.bootstrap.steps.components

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.configurations.ReloadProfile
import terapps.factoryplanner.bootstrap.dto.SatisfactoryStaticData
import terapps.factoryplanner.bootstrap.steps.RootStep
import terapps.factoryplanner.bootstrap.transformers.EntityTransformer
import terapps.factoryplanner.bootstrap.transformers.TransformerOrchestrator

@Component
class EntityCreationStep : RootStep, TransformerOrchestrator<EntityTransformer<Any, Any>>(EntityTransformer::class) {
    @Autowired
    override lateinit var satisfactoryStaticData: SatisfactoryStaticData

    @Autowired
    override lateinit var applicationContext: ApplicationContext

    @Autowired
    private lateinit var reloadProfile: ReloadProfile

    override val priority: Int
        get() = 1

    override fun prepare() {
        if (!reloadProfile.wipe) {
            logger.info("Skipping creation step: wipe disabled")
            return
        }
        val config = supportedCategories { category, transformer ->
            logger.info("Loading mapping category ${category.classType.simpleName} -> ${transformer.javaClass.simpleName}")
        }

        run(config) { gameEntity, transformer ->
            val transformed = transformer.transform(gameEntity)

            transformer.save(transformed)
        }

        registeredTransformers.forEach {
            it.batch.onBatch()
        }
    }

    override fun dispose() {}


}