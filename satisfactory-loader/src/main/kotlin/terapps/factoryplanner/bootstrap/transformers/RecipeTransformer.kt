package terapps.factoryplanner.bootstrap.transformers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.generated.FGRecipe
import terapps.factoryplanner.core.entities.Recipe
import terapps.factoryplanner.core.entities.RecipeRepository

@Component
class RecipeTransformer : AbstractTransformer<FGRecipe, Recipe>(FGRecipe::class) {
    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    override fun transform(transformIn: FGRecipe): Recipe {

        return transformIn.run {
            Recipe(
                    className = ClassName,
                    displayName = mDisplayName,
                    manufacturingDuration = mManufactoringDuration,
            )
        }
    }

    override fun save(output: Recipe): Recipe {
        return recipeRepository.save(output)

    }
}