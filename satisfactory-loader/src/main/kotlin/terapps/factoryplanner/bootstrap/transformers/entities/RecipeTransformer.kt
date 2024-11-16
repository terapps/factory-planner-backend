package terapps.factoryplanner.bootstrap.transformers.entities

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.generated.FGRecipe
import terapps.factoryplanner.bootstrap.transformers.AbstractTransformer
import terapps.factoryplanner.bootstrap.transformers.BatchList
import terapps.factoryplanner.core.entities.RecipeEntity
import terapps.factoryplanner.core.repositories.RecipeRepository

@Component
class RecipeTransformer : AbstractTransformer<FGRecipe, RecipeEntity>(FGRecipe::class) {
    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    override val batch: BatchList<RecipeEntity> = BatchList() {
        recipeRepository.saveAll(it)
    }

    override fun transform(transformIn: FGRecipe): RecipeEntity {

        return transformIn.run {
            RecipeEntity(
                    className = ClassName,
                    displayName = mDisplayName,
                    manufacturingDuration = mManufactoringDuration,
            )
        }
    }
}
