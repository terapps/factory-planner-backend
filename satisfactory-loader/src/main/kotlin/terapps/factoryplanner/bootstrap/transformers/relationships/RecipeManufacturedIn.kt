package terapps.factoryplanner.bootstrap.transformers.relationships

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.core.Neo4jClient
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.generated.FGRecipe
import terapps.factoryplanner.bootstrap.transformers.SatisfactoryRelationshipTransformer
import terapps.factoryplanner.bootstrap.transformers.entities.RecipeProducedInTransformer
import terapps.factoryplanner.bootstrap.extractDictEntry
import terapps.factoryplanner.bootstrap.toItemIO
import terapps.factoryplanner.core.entities.*

@Component
class RecipeManufacturedIn :
        SatisfactoryRelationshipTransformer<FGRecipe, Collection<Relationship>>(
                FGRecipe::class,
                RecipeEntity::class to CraftingMachineEntity::class,
                "MANUFACTURED_IN"
        ) {
    @Autowired
    override lateinit var neo4jClient: Neo4jClient

    @Autowired
    private lateinit var recipeProducedInTransformer: RecipeProducedInTransformer

    override var relationships: List<Relationship> = mutableListOf()

    lateinit var items: Collection<ItemDescriptorEntity>

    override fun transform(transformIn: FGRecipe): Collection<Relationship> {
        return transformIn.mProduct.extractDictEntry().mapNotNull {
            it.toItemIO { itemClass, out ->
                val actualItem = items.firstOrNull { it.className == itemClass }
                        ?: throw Error("Cannot find $itemClass in item descriptors")

                takeUnless { actualItem.category == ItemCategory.Raw }?.recipeProducedInTransformer?.transform(transformIn)?.map { Relationship(transformIn.ClassName, it) }
            }
        }.flatten()
    }
}