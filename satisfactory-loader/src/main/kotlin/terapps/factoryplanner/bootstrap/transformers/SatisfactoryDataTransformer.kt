package terapps.factoryplanner.bootstrap.transformers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.GameEntity
import terapps.factoryplanner.bootstrap.dto.generated.FGRecipe
import terapps.factoryplanner.core.entities.Recipe

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

    fun transform(gameEntity: GameEntity) {
        val delegateTransformers = listOf(craftingMachineTransformer, extractorTransformer, itemDescriptorTransformer, recipeTransformer) as List<SatisfactoryTransformer<Any, Any>>// TODO findable in DI ?
        val transformer = delegateTransformers.find { it.supportsClass(gameEntity.javaClass.kotlin) }

        transformer?.run {
            val result = save(transform(gameEntity))

            if (gameEntity is FGRecipe && result is Recipe) {
                itemDescriptorTransformer.attachRecipe(gameEntity, result)
            }
        }


    }
}