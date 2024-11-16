package terapps.factoryplanner.bootstrap.transformers.relationships

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.core.Neo4jClient
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.generated.FGRecipe
import terapps.factoryplanner.bootstrap.transformers.*
import terapps.factoryplanner.bootstrap.extractDictEntry
import terapps.factoryplanner.bootstrap.toItemIO
import terapps.factoryplanner.core.entities.ItemDescriptorEntity
import terapps.factoryplanner.core.entities.RecipeEntity

@Component
class ItemRequiredInRecipe:
        SatisfactoryRelationshipTransformer<FGRecipe, Collection<RelationshipItemIO>>(
                FGRecipe::class,
                ItemDescriptorEntity::class to RecipeEntity::class,
                "REQUIRED_IN"
        ) {
    @Autowired
    override lateinit var neo4jClient: Neo4jClient

    override var relationships: List<Relationship> = mutableListOf()

    override fun transform(transformIn: FGRecipe): Collection<RelationshipItemIO> {
        return transformIn.mIngredients.extractDictEntry().map {
            it.toItemIO { itemClass, out ->
                RelationshipItemIO(itemClass, transformIn.ClassName , out)
            }
        }
    }
}