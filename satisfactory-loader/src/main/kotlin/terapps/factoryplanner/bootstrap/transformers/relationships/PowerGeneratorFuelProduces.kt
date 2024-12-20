package terapps.factoryplanner.bootstrap.transformers.relationships

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.core.Neo4jClient
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.generated.FGBuildableGeneratorFuel
import terapps.factoryplanner.bootstrap.dto.generated.FGBuildableGeneratorNuclear
import terapps.factoryplanner.bootstrap.transformers.SatisfactoryRelationshipTransformer
import terapps.factoryplanner.core.entities.ItemDescriptorEntity
import terapps.factoryplanner.core.entities.PowerGeneratorEntity

@Component
class PowerGeneratorFuelProduces : SatisfactoryRelationshipTransformer<FGBuildableGeneratorFuel, Collection<Relationship>>(
        FGBuildableGeneratorFuel::class,
        PowerGeneratorEntity::class to ItemDescriptorEntity::class,
        "PRODUCED_BY"
) {
    @Autowired
    override lateinit var neo4jClient: Neo4jClient

    override var relationships: List<Relationship> = mutableListOf()

    override fun transform(transformIn: FGBuildableGeneratorFuel): Collection<Relationship> {
        return transformIn.mFuel.mapNotNull {
            val byproduct = it["mByproduct"]?.toString()?.takeUnless { it.isEmpty() }
            val byproductAmount = it["mByproduct"]?.toString()?.takeUnless { it.isEmpty() }?.toDouble()

            byproduct?.let {
                RelationshipItemIO(transformIn.ClassName, byproduct, byproductAmount!!)
            }
        }
    }
}

@Component
class PowerGeneratorFuelProducesNuclear : SatisfactoryRelationshipTransformer<FGBuildableGeneratorNuclear, Collection<Relationship>>(
        FGBuildableGeneratorNuclear::class,
        PowerGeneratorEntity::class to ItemDescriptorEntity::class,
        "PRODUCED_BY"
) {
    @Autowired
    override lateinit var neo4jClient: Neo4jClient

    override var relationships: List<Relationship> = mutableListOf()

    override fun transform(transformIn: FGBuildableGeneratorNuclear): Collection<Relationship> {
        return transformIn.mFuel.mapNotNull {
            val byproduct = it["mByproduct"]?.toString()?.takeUnless { it.isEmpty() }
            val byproductAmount = it["mByproductAmount"]?.toString()?.takeUnless { it.isEmpty() }?.toDouble()

            byproduct?.let {
                RelationshipItemIO(transformIn.ClassName, byproduct, byproductAmount!!)
            }
        }
    }
}