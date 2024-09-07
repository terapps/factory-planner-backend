package terapps.factoryplanner.bootstrap.steps

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import terapps.factoryplanner.bootstrap.dto.generated.FGRecipe
import terapps.factoryplanner.bootstrap.steps.components.NoOp
import terapps.factoryplanner.bootstrap.steps.components.RecipeStep
import terapps.factoryplanner.bootstrap.steps.components.Step
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf


@Service
class StepManager {

    @Autowired
    private lateinit var recipeStep: RecipeStep
    private lateinit var steps: Map<KClass<*>, Step>

    @PostConstruct
    fun createSteps() {
        steps = mapOf(
                FGRecipe::class to recipeStep
        )
    }

    fun prepareStep(classType: KClass<*>) {
        steps[classType]?.run {
            prepare()
        }
    }

    fun disposeStep(classType: KClass<*>) {
        steps[classType]?.run {
            dispose()
        }
    }
}