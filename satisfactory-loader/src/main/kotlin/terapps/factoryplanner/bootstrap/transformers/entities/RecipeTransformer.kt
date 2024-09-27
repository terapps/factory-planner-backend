package terapps.factoryplanner.bootstrap.transformers.entities

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.generated.FGRecipe
import terapps.factoryplanner.bootstrap.transformers.AbstractTransformer
import terapps.factoryplanner.bootstrap.transformers.BatchList
import terapps.factoryplanner.core.entities.Extractor
import terapps.factoryplanner.core.entities.Recipe
import terapps.factoryplanner.core.repositories.RecipeRepository

@Component
class RecipeTransformer : AbstractTransformer<FGRecipe, Recipe>(FGRecipe::class) {
    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    override val batch: BatchList<Recipe> = BatchList() {
        recipeRepository.saveAll(it)
    }

    override fun transform(transformIn: FGRecipe): Recipe {

        return transformIn.run {
            Recipe(
                    className = ClassName,
                    displayName = mDisplayName,
                    manufacturingDuration = mManufactoringDuration,
            )
        }
    }
}
