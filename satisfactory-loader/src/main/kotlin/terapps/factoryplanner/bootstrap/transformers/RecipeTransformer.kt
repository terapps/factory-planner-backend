package terapps.factoryplanner.bootstrap.transformers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.generated.FGRecipe
import terapps.factoryplanner.core.entities.*

@Component
class RecipeTransformer : AbstractTransformer<FGRecipe, Recipe>(FGRecipe::class) {
    @Autowired
    private lateinit var recipeRequiresTransformer: RecipeRequiresTransformer

    @Autowired
    private lateinit var recipeProducedInTransformer: RecipeProducedInTransformer

    @Autowired
    private lateinit var recipeProducingTransformer: RecipeProducingTransformer

    @Autowired
    private lateinit var recipeRepository: RecipeRepository

    override fun transform(transformIn: FGRecipe): Recipe {

        return transformIn.run {

            val producedIn = mProducedIn.extractListEntry().filter { it.isNotEmpty() }.flatMap {
                recipeProducedInTransformer.transform(this to it)
            }

            val manualCrafting = producedIn.firstOrNull { it is CraftingMachine && it.id == "manual_crafting" }
            val automatedCrafting = producedIn.firstOrNull { it is CraftingMachine && it.id != "manual_crafting" }
            val extractors: List<Extractor> = producedIn.filterIsInstance<List<*>>().flatten() as List<Extractor>

            if (listOfNotNull(extractors, automatedCrafting, manualCrafting).isEmpty()) {
                throw Error("at least one machine is required for recipe class $ClassName")
            }

            if (extractors.size > 1) {
                throw Error("You cant change to one extractor")
            }

            Recipe(
                    id = ClassName,
                    displayName = mDisplayName,
                    manufacturingDuration = mManufactoringDuration,
                    producedIn = automatedCrafting as CraftingMachine?,
                    extractedIn = extractors.toSet(),
            ).apply {
                if (mIngredients != mProduct) { // Raw resources has cyclic ingredient/produces
                    ingredients = mIngredients.extractDictEntry().map { // TODO transformer
                        recipeRequiresTransformer.transform(it)
                    }.toSet()
                }
/*                producing = mProduct.extractDictEntry().map {
                    recipeProducingTransformer.transform(this to it)
                }.toSet()      */      }
        }
    }

    override fun save(output: Recipe): Recipe = recipeRepository.save(output)
}
