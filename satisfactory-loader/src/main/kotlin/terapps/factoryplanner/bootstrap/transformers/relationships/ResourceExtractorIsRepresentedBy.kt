package terapps.factoryplanner.bootstrap.transformers.relationships

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.core.Neo4jClient
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.generated.FGBuildableResourceExtractor
import terapps.factoryplanner.bootstrap.transformers.SatisfactoryRelationshipTransformer
import terapps.factoryplanner.core.entities.ExtractorEntity
import terapps.factoryplanner.core.entities.ItemDescriptorEntity

@Component
class ResourceExtractorIsRepresentedBy : SatisfactoryRelationshipTransformer<FGBuildableResourceExtractor, Collection<Relationship>>(
        FGBuildableResourceExtractor::class,
        ExtractorEntity::class to ItemDescriptorEntity::class,
        "IS_REPRESENTED_BY"
) {
    @Autowired
    override lateinit var neo4jClient: Neo4jClient

    override var relationships: List<Relationship> = mutableListOf()

    override fun transform(transformIn: FGBuildableResourceExtractor): Collection<Relationship> {
        return listOf(Relationship(transformIn.ClassName, transformIn.ClassName.replace("Build", "Desc")))
    }
}
