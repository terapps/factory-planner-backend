package terapps.factoryplanner.bootstrap.transformers.relationships

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.core.Neo4jClient
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.generated.FGRecipe
import terapps.factoryplanner.bootstrap.transformers.SatisfactoryRelationshipTransformer
import terapps.factoryplanner.bootstrap.extractDictEntry
import terapps.factoryplanner.bootstrap.toItemIO
import terapps.factoryplanner.core.entities.ExtractorEntity
import terapps.factoryplanner.core.entities.ItemCategory
import terapps.factoryplanner.core.entities.ItemDescriptorEntity

@Component
class ItemExtractedIn : SatisfactoryRelationshipTransformer<FGRecipe, Collection<Relationship>>(
        FGRecipe::class,
        ItemDescriptorEntity::class to ExtractorEntity::class,
        "EXTRACTED_IN"
) {
    @Autowired
    override lateinit var neo4jClient: Neo4jClient

    override var relationships: List<Relationship> = mutableListOf()

    // TODO could be better?
    lateinit var items: Collection<ItemDescriptorEntity>
    lateinit var extractors: Collection<ExtractorEntity>

    override fun transform(transformIn: FGRecipe): Collection<Relationship> {
        return transformIn.mProduct.extractDictEntry().mapNotNull {
            it.toItemIO { itemClass, out ->
                val actualItem = items.firstOrNull { it.className == itemClass }
                        ?: throw Error("Cannot find $itemClass in item descriptors")

                takeIf { actualItem.category == ItemCategory.Raw }?.extractors?.filter {
                    if (it.matchByAllowedResources) it.allowedResources.contains(actualItem.className) else it.allowedResourceForm.contains(actualItem.form)
                }?.map { extractor ->
                    Relationship(itemClass, extractor.className)
                }
            }
        }.flatten()
    }
}