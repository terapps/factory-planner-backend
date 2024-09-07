package terapps.factoryplanner.bootstrap.steps.components

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.generated.FGRecipe
import terapps.factoryplanner.bootstrap.transformers.SatisfactoryDataTransformer
import terapps.factoryplanner.core.entities.CraftingMachine
import terapps.factoryplanner.core.entities.CraftingMachineRepository
import terapps.factoryplanner.core.entities.ExtractorRepository

@Component
class RecipeStep : Step {
    @Autowired
    private lateinit var extractorRepository: ExtractorRepository

    @Autowired
    private lateinit var satisfactoryDataTransformer: SatisfactoryDataTransformer

    @Autowired
    private lateinit var craftingMachineRepository: CraftingMachineRepository

    override fun prepare() {
        println("Running prepare recipe")
        craftingMachineRepository.save(CraftingMachine(
                "manual_crafting",
                displayName = "Manual Crafting",
                description = "Wouf",
                manual = true
        ))

        val waterExtractor = extractorRepository.findById("Build_WaterPump_C").get()

        satisfactoryDataTransformer.transform(FGRecipe(
                "Recipe_Water_C",
                "Water extraction recipe",
                "Water extraction recipe",
                "",
                0f,
                waterExtractor.extractCycleTime,
                0f,
                "(SelfMade.Build_WaterPump_C)",
                "((ItemClass=BlueprintGeneratedClass'\"/Game/FactoryGame/Resource/Raw.Desc_Water_C\"',Amount=1))",
                "",
                0f,
                0f
        ))
    }
}