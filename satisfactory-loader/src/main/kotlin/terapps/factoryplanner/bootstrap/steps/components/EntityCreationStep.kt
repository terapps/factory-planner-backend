package terapps.factoryplanner.bootstrap.steps.components

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.configuration.ReloadProfile
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


    override fun prepare() {
        if (!reloadProfile.wipe) {
            return
        }
        val config = supportedCategories { category, transformer ->
            println("[${this.javaClass.simpleName}] Loading mapping category ${category.classType.simpleName} -> ${transformer.javaClass.simpleName}")
        }

        run(config) { gameEntity, transformer ->
            transformer.save(transformer.transform(gameEntity))
        }
    }

    override fun dispose() {}


}