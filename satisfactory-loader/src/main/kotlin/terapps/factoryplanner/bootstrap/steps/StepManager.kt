package terapps.factoryplanner.bootstrap.steps

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import terapps.factoryplanner.bootstrap.dto.GameObjectCategory
import terapps.factoryplanner.bootstrap.steps.components.*
import terapps.factoryplanner.bootstrap.transformers.ExtractorTransformer
import terapps.factoryplanner.bootstrap.transformers.RecipeTransformer
import terapps.factoryplanner.bootstrap.transformers.SatisfactoryTransformer
import kotlin.reflect.KClass

@Service
class StepManager {

    @Autowired
    private lateinit var recipeStep: RecipeStep

    @Autowired
    private lateinit var cleanDataStep: CleanDataStep


    private lateinit var steps: Map<KClass<*>, Step>

    private lateinit var rootSteps: List<RootStep>

    @PostConstruct
    fun createSteps() {
        steps = mapOf(
                RecipeTransformer::class to recipeStep,
        )
        rootSteps = listOf(
                cleanDataStep
        )
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

    fun prepareStep(category: GameObjectCategory<Any>, transformer: SatisfactoryTransformer<Any, Any>) {
        steps[category.classType]?.run {
            prepare(category)
        }
        steps[transformer.javaClass.kotlin]?.run {
            prepare(category)
        }
    }

    fun disposeStep(category: GameObjectCategory<Any>, transformer: SatisfactoryTransformer<Any, Any>) {
        steps[category.classType]?.run {
            dispose(category)
        }
        steps[transformer.javaClass.kotlin]?.run {
            dispose(category)
        }

    }
}