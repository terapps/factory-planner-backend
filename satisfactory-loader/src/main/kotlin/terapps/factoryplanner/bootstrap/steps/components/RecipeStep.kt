package terapps.factoryplanner.bootstrap.steps.components

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.core.Neo4jClient
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.configuration.ReloadProfile
import terapps.factoryplanner.bootstrap.dto.GameObjectCategory
import terapps.factoryplanner.bootstrap.dto.generated.FGRecipe
import terapps.factoryplanner.bootstrap.transformers.RecipeProducedInTransformer
import terapps.factoryplanner.bootstrap.transformers.RecipeTransformer
import terapps.factoryplanner.bootstrap.transformers.extractDictEntry
import terapps.factoryplanner.bootstrap.transformers.toItemIO
import terapps.factoryplanner.core.entities.*


@Component
class RecipeStep : Step {
    @Autowired
    private lateinit var extractorRepository: ExtractorRepository

    @Autowired
    private lateinit var recipeTransformer: RecipeTransformer

    @Autowired
    private lateinit var craftingMachineRepository: CraftingMachineRepository

    @Autowired
    private lateinit var reloadProfile: ReloadProfile

    @Autowired
    private lateinit var recipeRepository: RecipeRepository


    @Autowired
    private lateinit var itemDescriptorRepository: ItemDescriptorRepository

    @Autowired
    private lateinit var recipeProducedInTransformer: RecipeProducedInTransformer

    @Autowired
    private lateinit var neo4jClient: Neo4jClient

    override fun prepare(category: GameObjectCategory<Any>) {
        if (reloadProfile.wipe || reloadProfile.steps.contains("FGRecipe")) {
            println("Cleaning old recipe...")
            recipeRepository.deleteAll()
        }
        println("Running prepare recipe")

        craftingMachineRepository.save(CraftingMachine(
                "manual_crafting",
                displayName = "Manual Crafting",
                description = "Wouf",
                manual = true
        ))
        val waterExtractor = extractorRepository.findByClassName("Build_WaterPump_C")!!

        category.Classes += FGRecipe(
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
        )
    }


    fun createRelationshipProducedBy(test: List<Map<String, Any>>) {
        val cypherQuery = "UNWIND \$relationships AS relationship " +
                "MATCH (p:Recipe {className: relationship.firstId}), (m:ItemDescriptor {className: relationship.secondId}) " +
                "CREATE (p)-[:PRODUCED_BY { outputPerCycle: relationship.out }]->(m)"
        neo4jClient.query(cypherQuery)
                .bind(test).to("relationships")
                .run()
    }

    fun createRelationshipRequiredIn(test: List<Map<String, Any>>) {
        val cypherQuery = "UNWIND \$relationships AS relationship " +
                "MATCH (p:Recipe {className: relationship.firstId}), (m:ItemDescriptor {className: relationship.secondId}) " +
                "CREATE (p)<-[:REQUIRED_IN { outputPerCycle: relationship.out }]-(m)"
        neo4jClient.query(cypherQuery)
                .bind(test).to("relationships")
                .run()
    }

    fun createRelationshipProducedIn(test: List<Map<String, Any>>) {
        val cypherQuery = "UNWIND \$relationships AS relationship " +
                "MATCH (p:Recipe {className: relationship.firstId}), (m:CraftingMachine {className: relationship.secondId}) " +
                "CREATE (p)<-[:MANUFACTURED_IN]-(m)"
        neo4jClient.query(cypherQuery)
                .bind(test).to("relationships")
                .run()
    }

    fun createRelationshipExtractedIn(test: List<Map<String, Any>>) {
        val cypherQuery = "UNWIND \$relationships AS relationship " +
                "MATCH (p:ItemDescriptor {className: relationship.firstId}), (m:Extractor {className: relationship.secondId}) " +
                "CREATE (p)-[:EXTRACTED_IN]->(m)"
        neo4jClient.query(cypherQuery)
                .bind(test).to("relationships")
                .run()
    }

    private fun toRel(firstId: String, secondId: String, out: Float) = mapOf(
            "firstId" to firstId,
            "secondId" to secondId,
            "out" to out.toString() // floats are stored as string in neoj.
    )

    private fun toRel(firstId: String, secondId: String) = mapOf(
            "firstId" to firstId,
            "secondId" to secondId,
    )

    private fun String.toRelationshipMapping(firstId: String) = this.extractDictEntry().map {
        it.toItemIO { itemClass, out ->
            toRel(firstId, itemClass, out)
        }
    }.toMutableList()

    override fun dispose(category: GameObjectCategory<Any>) {
        val ingredients = mutableListOf<Map<String, Any>>()
        val products = mutableListOf<Map<String, Any>>()
        val extractors = mutableListOf<Map<String, Any>>()
        val machines = mutableListOf<Map<String, Any>>()

        // TODO projection would be better here
        val items = itemDescriptorRepository.findAll()
        val savedExtractors = extractorRepository.findAll()

        category.Classes.forEach { recipe ->
            if (recipe !is FGRecipe) {
                throw Exception("${recipe} is not ${FGRecipe::class}")
            }

            ingredients.addAll(recipe.mIngredients.toRelationshipMapping(recipe.ClassName))
            products.addAll(recipe.mProduct.toRelationshipMapping(recipe.ClassName).onEach { producesRelationship ->
                val actualItem = items.first { it.className == producesRelationship["secondId"] }

                if (actualItem.category == ItemCategory.Raw) {
                    extractors.addAll(savedExtractors.filter {
                        (it.allowedResources.isEmpty() && it.allowedResourceForm.contains(actualItem.form)) ||
                                (it.allowedResources.contains(actualItem.className) && it.allowedResourceForm.contains(actualItem.form))
                    }.map { toRel(actualItem.className!!, it.className) })
                } else {
                    val producedIn = recipeProducedInTransformer.transform(recipe)

                    machines.addAll(producedIn.map { toRel(producesRelationship["firstId"] as String, it) })
                }
            })
        }


        createRelationshipProducedBy(products)
        createRelationshipRequiredIn(ingredients)
        createRelationshipProducedIn(machines.distinctBy { it["firstId"] to it["secondId"] })
        createRelationshipExtractedIn(extractors.distinctBy { it["firstId"] to it["secondId"] })
    }
}