package terapps.factoryplanner.bootstrap.transformers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.generated.FGRecipe
import terapps.factoryplanner.core.entities.*

@Component
class RecipeProducedInTransformer : SatisfactoryTransformer<
        Pair<FGRecipe, ItemRef>, List<Automaton>> {
    @Autowired
    private lateinit var itemDescriptorRepository: ItemDescriptorRepository

    @Autowired
    private lateinit var craftingMachineRepository: CraftingMachineRepository

    @Autowired
    private lateinit var extractorRepository: ExtractorRepository

    override fun transform(transformIn: Pair<FGRecipe, ItemRef>): List<Automaton> {
        val (fgRecipe, itemRef) = transformIn
        val blueprintClassRegex = ".*\\.(.*)".toRegex()

        return when (val descriptor = blueprintClassRegex.matchEntire(itemRef)!!.groupValues[1]) {
            "BP_BuildGun_C",
            "FGBuildGun",
            "Build_AutomatedWorkBench_C",
            "BP_WorkBenchComponent_C",
            "FGBuildableAutomatedWorkBench",
            "BP_WorkshopComponent_C" -> listOf(craftingMachineRepository.findById("manual_crafting").orElseThrow { throw Error("Manual CraftingMaching ${descriptor} not found") })
            "Build_WaterPump_C" -> listOf(extractorRepository.findById(descriptor).orElseThrow { throw Error("Extractor water pump not found") })
            "Build_Converter_C" -> {
                val recipeProduces = fgRecipe.mProduct.extractDictEntry()
                val produces = itemDescriptorRepository.findByIdIn(recipeProduces.map { it.toItemIO { idClassName, _ -> idClassName } })

                if (produces.size != 1) {
                    throw Error("Expected only one produced item for raw resource recipe, got: $produces")
                }
                val producedItem = produces.first()
                // TODO make query
                val extractors = extractorRepository.findAll().filter { it.allowedResources.any { it == producedItem.id } }

                extractors
            }

            else -> listOf(craftingMachineRepository.findById(descriptor).orElseThrow { throw Error("Crafting maching ${descriptor} not found") })
        }

    }

    override fun save(output: List<Automaton>): List<Automaton> {
        TODO("Not implemented: Should not be called standalone")
    }
}
