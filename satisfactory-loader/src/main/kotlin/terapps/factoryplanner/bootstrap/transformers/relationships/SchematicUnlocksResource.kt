package terapps.factoryplanner.bootstrap.transformers.relationships

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.core.Neo4jClient
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.generated.FGSchematic
import terapps.factoryplanner.bootstrap.extractDictEntry
import terapps.factoryplanner.core.services.components.Relationship
import terapps.factoryplanner.bootstrap.transformers.SatisfactoryRelationshipTransformer
import terapps.factoryplanner.core.entities.*

@Component
class SchematicUnlocksResource : SatisfactoryRelationshipTransformer<FGSchematic, Collection<Relationship>>(
        FGSchematic::class,
        SchematicEntity::class to ItemDescriptorEntity::class,
        "SCHEMATIC_UNLOCKS_RESOURCE"
) {
    @Autowired
    override lateinit var neo4jClient: Neo4jClient

    override var relationships: List<Relationship> = mutableListOf()

    override fun transform(transformIn: FGSchematic): Collection<Relationship> {
        return transformIn.mUnlocks.filter {
            it["Class"] == "BP_UnlockScannableResource_C"
        }.map {
            (it["mResourcePairsToAddToScanner"] as String).extractDictEntry().map {
                val descriptor = it["ResourceDescriptor"]
                val blueprintClassRegex = ".*BlueprintGeneratedClass'.*\\.(?<captured>.*)'".toRegex()
                val recipeDescriptor = blueprintClassRegex.find(descriptor!!)?.groups?.get("captured")?.value ?: throw Error("SchematicUnlocksResource: doesnt match regex $descriptor")

                Relationship(transformIn.ClassName, recipeDescriptor)
            }
        }.flatten()
    }
}