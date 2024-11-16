package terapps.factoryplanner.bootstrap.transformers.relationships

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.core.Neo4jClient
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.generated.FGSchematic
import terapps.factoryplanner.bootstrap.extractListEntry
import terapps.factoryplanner.bootstrap.transformers.SatisfactoryRelationshipTransformer
import terapps.factoryplanner.core.entities.*

@Component
class SchematicUnlocksRecipe : SatisfactoryRelationshipTransformer<FGSchematic, Collection<Relationship>>(
        FGSchematic::class,
        SchematicEntity::class to RecipeEntity::class,
        "SCHEMATIC_UNLOCKS_RECIPE"
) {
    @Autowired
    override lateinit var neo4jClient: Neo4jClient

    override var relationships: List<Relationship> = mutableListOf()

    override fun transform(transformIn: FGSchematic): Collection<Relationship> {
        return transformIn.mUnlocks.filter {
            it["Class"] == "BP_UnlockRecipe_C"
        }.map {
            (it["mRecipes"] as String).extractListEntry().map {
                val blueprintClassRegex = ".*BlueprintGeneratedClass'.*\\.(?<captured>.*)'".toRegex()
                val recipeDescriptor = blueprintClassRegex.find(it)?.groups?.get("captured")?.value ?: throw Error("ItemIO: doesnt match regex $it")

                Relationship(transformIn.ClassName, recipeDescriptor)
            }
        }.flatten()
    }
}