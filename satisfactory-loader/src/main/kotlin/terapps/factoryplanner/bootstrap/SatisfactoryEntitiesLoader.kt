package terapps.factoryplanner.bootstrap

import FGAmmoTypeInstantHit
import FGAmmoTypeProjectile
import FGAmmoTypeSpreadshot
import FGBuildableManufacturer
import FGBuildableManufacturerVariablePower
import FGBuildableResourceExtractor
import FGBuildableWaterPump
import FGBuildingDescriptor
import FGConsumableDescriptor
import FGEquipmentDescriptor
import FGItemDescriptor
import FGItemDescriptorBiomass
import FGItemDescriptorNuclearFuel
import FGPoleDescriptor
import FGRecipe
import FGResourceDescriptor
import FGSchematic
import FGVehicleDescriptor
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import terapps.factoryplanner.bootstrap.dto.GameObjectCategory
import terapps.factoryplanner.core.entities.*
import terapps.factoryplanner.services.bootstrap.extractDictEntry
import terapps.factoryplanner.services.bootstrap.extractListEntry
import terapps.factoryplanner.bootstrap.transformers.toCraftingMachine
import terapps.factoryplanner.bootstrap.transformers.toExtractor
import terapps.factoryplanner.bootstrap.transformers.toItemDescriptor
import kotlin.jvm.optionals.getOrNull

@Service
class SatisfactoryEntitiesLoader {
    @Value("classpath:data.json")
    lateinit var resourceFile: Resource

    @Value("\${satisfactory.planner.data.force-reload}")
    var forceReload: Boolean? = null

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var itemDescriptorRepository: ItemDescriptorRepository

    @Autowired
    lateinit var craftingMachineRepository: CraftingMachineRepository

    @Autowired
    lateinit var extractorRepository: ExtractorRepository

    @Autowired
    lateinit var recipeRepository: RecipeRepository

    @Autowired
    lateinit var schematicRepository: SchematicRepository

    @Autowired
    lateinit var recipeWeightService: RecipeWeightService

    private fun parseSatisfactoryJson() = objectMapper.readValue(
            resourceFile.file,
            object : TypeReference<List<GameObjectCategory>>() {}
    )

    fun loadBaseEntities(gameObjects: List<GameObjectCategory>) {
        val (resources, items) = gameObjects.flatMap { it.Classes }.partition { it is FGResourceDescriptor }

        resources.forEach {
            when (it) {
                is FGResourceDescriptor -> it.toItemDescriptor().run {
                    itemDescriptorRepository.save(this)
                }
            }
        }

        items.forEach {
            when (it) {
                is FGBuildableManufacturerVariablePower -> it.toCraftingMachine().run {
                    craftingMachineRepository.save(this)
                }

                is FGBuildableManufacturer -> it.toCraftingMachine().run {
                    craftingMachineRepository.save(this)
                }

                is FGBuildableResourceExtractor -> it.toExtractor(itemDescriptorRepository).run {
                    extractorRepository.save(this)
                }

                is FGBuildableWaterPump -> it.toExtractor(itemDescriptorRepository).run {
                    extractorRepository.save(this)
                }

                is FGItemDescriptor -> it.toItemDescriptor().run {
                    itemDescriptorRepository.save(this)
                }

                is FGItemDescriptorBiomass -> it.toItemDescriptor().run {
                    itemDescriptorRepository.save(this)
                }

                is FGItemDescriptorNuclearFuel -> it.toItemDescriptor().run {
                    itemDescriptorRepository.save(this)
                }

                is FGBuildingDescriptor -> it.toItemDescriptor().run {
                    itemDescriptorRepository.save(this)
                }

                is FGPoleDescriptor -> it.toItemDescriptor().run {
                    itemDescriptorRepository.save(this)
                }

                is FGEquipmentDescriptor -> it.toItemDescriptor().run {
                    itemDescriptorRepository.save(this)
                }

                is FGAmmoTypeProjectile -> it.toItemDescriptor().run {
                    itemDescriptorRepository.save(this)
                }

                is FGVehicleDescriptor -> it.toItemDescriptor().run {
                    itemDescriptorRepository.save(this)
                }

                is FGConsumableDescriptor -> it.toItemDescriptor().run {
                    itemDescriptorRepository.save(this)
                }

                is FGAmmoTypeSpreadshot -> it.toItemDescriptor().run {
                    itemDescriptorRepository.save(this)
                }

                is FGAmmoTypeInstantHit -> it.toItemDescriptor().run {
                    itemDescriptorRepository.save(this)
                }

                else -> {
                    /*                    if (it.isDescriptor()) {
                                            itemDescriptorRepository.save(it.toItemDescriptor())
                                        }*/

                }
            }
        }
    }


    private fun Map<String, String>.toItemIO(): ItemIO {
        val blueprintClassRegex = "BlueprintGeneratedClass'\".*\\.(.*)\"'".toRegex()
        val descriptor = blueprintClassRegex.matchEntire(this["ItemClass"]!!)!!.groupValues[1]
        val item = itemDescriptorRepository.findById(descriptor).getOrNull()
                ?: throw Error("Could not transform ${descriptor}")
        val out = if (item.form == "RF_LIQUID") {
            // convert fluid cubic meters values
            this["Amount"]!!.toFloat() / 1000f
        } else this["Amount"]!!.toFloat()


        return ItemIO(outputPerCycle = out, descriptor = item)
    }

    fun loadRecipe(recipes: List<FGRecipe>) =
            recipes.forEachIndexed { index, fgRecipe ->
                val produces = fgRecipe.mProduct.extractDictEntry().map {
                    it.toItemIO()
                }.toSet()
                val ingredients = fgRecipe.mIngredients.extractDictEntry().map {
                    it.toItemIO()
                }.toSet()
                val producedIn = fgRecipe.mProducedIn.extractListEntry().filter { it.isNotEmpty() }.map {
                    val blueprintClassRegex = ".*\\.(.*)".toRegex()
                    when (val descriptor = blueprintClassRegex.matchEntire(it)!!.groupValues[1]) {
                        "BP_BuildGun_C",
                        "FGBuildGun",
                        "Build_AutomatedWorkBench_C",
                        "BP_WorkBenchComponent_C",
                        "FGBuildableAutomatedWorkBench",
                        "BP_WorkshopComponent_C" -> craftingMachineRepository.findById("manual_crafting")

                        "Build_Converter_C" -> {
                            if (produces.size != 1) {
                                throw Error("Expected only one produced item for raw resource recipe, got: $produces")
                            }
                            val producedItem = produces.first().descriptor
                            val extractors = extractorRepository.findAll().filter { it.allowedResources.any { it.id == producedItem.id } }
                            extractors

                        }

                        else -> craftingMachineRepository.findById(descriptor)
                    }
                }
                val manualCrafting = producedIn.firstOrNull { it is CraftingMachine && it.id == "manual_crafting" }
                val automatedCrafting: CraftingMachine? = producedIn.firstOrNull { it is CraftingMachine && it.id != "manual_crafting" } as? CraftingMachine?
                val extractors: List<Extractor> = producedIn.filterIsInstance<List<*>>().flatMap {
                    it
                } as List<Extractor>



                if (listOfNotNull(extractors, automatedCrafting, manualCrafting).isEmpty()) {
                    throw Error("Wouf, at least one machine please")
                }

                println("Loading recipe ${fgRecipe.ClassName}, ${index}/${recipes.size}")
                val recipe = Recipe(
                        id = fgRecipe.ClassName,
                        displayName = fgRecipe.mDisplayName,
                        manufacturingDuration = fgRecipe.mManufactoringDuration.toFloat(),
                        ingredients = ingredients,
                        produces = produces,
                        producedIn = automatedCrafting,
                        extractedIn = extractors.toSet()
                )

                recipeRepository.save(recipe)
            }

    private fun loadSchematic(schematics: List<FGSchematic>) {
        /*        schematics.forEach {
                    val schematic = Schematic(
                            id = it.ClassName!!,
                            displayName = it.mDisplayName!!,
                            description = it.mDescription!!,
                            type = it.mType!!,
                            cost = it.mCost!!.extractDictEntry().map { it.toItemIO() }.toSet()
                    ).apply {

                    }
                }*/
    }

    @PostConstruct
    fun init() {
        if (forceReload == true) {
            itemDescriptorRepository.deleteAll()
            craftingMachineRepository.deleteAll()
            recipeRepository.deleteAll()
            extractorRepository.deleteAll()

            craftingMachineRepository.save(CraftingMachine(
                    "manual_crafting",
                    displayName = "Manual Crafting",
                    description = "Wouf",
                    manual = true
            ))
            val json = parseSatisfactoryJson()

            val (priortyObjects, recipes) = json.partition { gameCategory ->
                val category = !gameCategory.isRecipe()


                println("Loading category ${gameCategory.NativeClass} : ${category}")
                category
            }
            loadBaseEntities(priortyObjects)

            val waterExtractor = extractorRepository.findById("Build_WaterPump_C").get()

            loadRecipe(recipes.find { it.isRecipe() }!!.Classes.run {
                plus(FGRecipe(
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
            } as List<FGRecipe>)
        }

        val loadedRecipes = recipeRepository.findAll()

        loadedRecipes.forEach {
            recipeWeightService.compute(it)
            recipeRepository.save(it)
        }

    }
}