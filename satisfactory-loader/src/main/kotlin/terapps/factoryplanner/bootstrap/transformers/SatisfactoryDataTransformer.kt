package terapps.factoryplanner.bootstrap.transformers

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.GameObjectCategory
import terapps.factoryplanner.bootstrap.steps.StepManager
import kotlin.reflect.KClass

@Component
class SatisfactoryDataTransformer {
    @Autowired
    private lateinit var craftingMachineTransformer: CraftingMachineTransformer

    @Autowired
    private lateinit var extractorTransformer: ExtractorTransformer

    @Autowired
    private lateinit var itemDescriptorTransformer: ItemDescriptorTransformer

    @Autowired
    private lateinit var recipeTransformer: RecipeTransformer

    private lateinit var delegateTransformers: List<SatisfactoryTransformer<Any, Any>>

    @Autowired
    private lateinit var stepManager: StepManager

    @PostConstruct
    fun init() {
        delegateTransformers = listOf(craftingMachineTransformer, extractorTransformer, itemDescriptorTransformer, recipeTransformer) as List<SatisfactoryTransformer<Any, Any>>// TODO findable in DI ?
    }

    private fun resolveTransformerClass(clazz: KClass<Any>) = delegateTransformers.find { it.supportsClass(clazz) }

    private fun transform(gameEntity: Any, transformer: SatisfactoryTransformer<Any, Any>) {
        transformer.run {
            save(transform(gameEntity))
        }
    }

    fun transformCategory(gameObjectCategory: GameObjectCategory<Any>, onload: (category: GameObjectCategory<Any>) -> Unit = {}) {
        val resolvedTransformer = resolveTransformerClass(gameObjectCategory.classType)

        resolvedTransformer?.let { transformer ->
            stepManager.prepareStep(gameObjectCategory, transformer)

            onload(gameObjectCategory)
            gameObjectCategory.Classes.forEachIndexed { index, gameObject ->
                println("Loading class ${gameObject.javaClass} with transformer $${transformer.javaClass}: ${index + 1} / ${gameObjectCategory.Classes.size}")
                transform(gameObject, transformer)
            }

            stepManager.disposeStep(gameObjectCategory, transformer)
        }
    }
}