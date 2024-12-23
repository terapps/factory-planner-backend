package terapps.factoryplanner.bootstrap.transformers.relationships

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.neo4j.core.Neo4jClient
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.generated.FGBuildableGeneratorFuel
import terapps.factoryplanner.bootstrap.dto.generated.FGBuildableGeneratorNuclear
import terapps.factoryplanner.core.services.components.Relationship
import terapps.factoryplanner.core.services.components.RelationshipItemIO
import terapps.factoryplanner.core.services.components.RelationshipItemIOBurner
import terapps.factoryplanner.bootstrap.transformers.SatisfactoryRelationshipTransformer
import terapps.factoryplanner.core.entities.ItemDescriptorEntity
import terapps.factoryplanner.core.entities.PowerGeneratorEntity

data class PowerGeneratorBurnerMetadata(
        val burnTime: Double,
        val fuelRequiredPerMinute: Double,
        val additionnalResourceRequiredPerMinute: Double? = null,
) {
    val nbCyclePerMinute: Double
        get() {
            return 60.0 / burnTime
        }

    val fuelRequiredPerCycle: Double
        get() {

            return fuelRequiredPerMinute / nbCyclePerMinute
        }

    val additionnalResourceRequiredPerCycle: Double?
        get() {
            return additionnalResourceRequiredPerMinute?.let { it / nbCyclePerMinute }
        }
}

val fuelMetadata = mapOf(
        "Desc_Coal_C" to PowerGeneratorBurnerMetadata(4.0, 15.0, 45.0),
        "Desc_CompactedCoal_C" to PowerGeneratorBurnerMetadata(8.4, 7.142857, 45.0),
        "Desc_PetroleumCoke_C" to PowerGeneratorBurnerMetadata(2.4, 25.0, 45.0),

        "Desc_LiquidFuel_C" to PowerGeneratorBurnerMetadata(3.0, 20.0),
        "Desc_LiquidBiofuel_C" to PowerGeneratorBurnerMetadata(3.0, 20.0),
        "Desc_LiquidTurboFuel_C" to PowerGeneratorBurnerMetadata(8.0, 7.5),
        "Desc_RocketFuel_C" to PowerGeneratorBurnerMetadata(14.4, 4.1666),
        "Desc_IonizedFuel_C" to PowerGeneratorBurnerMetadata(20.0, 3.0),

        "Desc_NuclearFuelRod_C" to PowerGeneratorBurnerMetadata(300.0, 0.2, 240.0),
        "Desc_PlutoniumFuelRod_C" to PowerGeneratorBurnerMetadata(600.0, 0.1, 240.0),
        "Desc_FicsoniumFuelRod_C" to PowerGeneratorBurnerMetadata(60.0, 1.0, 240.0),


        // TODO improve, fake meta
        "Desc_Leaves_C" to PowerGeneratorBurnerMetadata(0.0, 80000.0),
        "Desc_Wood_C" to PowerGeneratorBurnerMetadata(0.0, 80000.0),
        "Desc_Mycelia_C" to PowerGeneratorBurnerMetadata(0.0, 80000.0),
        "Desc_GenericBiomass_C" to PowerGeneratorBurnerMetadata(0.0, 80000.0),
        "Desc_Biofuel_C" to PowerGeneratorBurnerMetadata(0.0, 80000.0),
        "Desc_PackagedBiofuel_C" to PowerGeneratorBurnerMetadata(0.0, 80000.0),
)

fun transformFuel(className: String, fuel: Set<Map<String, Any>>): Collection<Relationship> {
    return fuel.map { fuelReq ->
        val fuelClass = fuelReq["mFuelClass"]!!.toString()
        val meta = fuelMetadata[fuelClass] ?: throw Error("${fuelClass} no meta")
        val fuelAmount = meta.fuelRequiredPerCycle
        val burnTime = meta.burnTime
        val supplementalResource = fuelReq["mSupplementalResourceClass"]?.toString()?.takeUnless { it.isEmpty() }
        val supplementalResourceAmount = meta.additionnalResourceRequiredPerCycle
        val rel = mutableListOf<Relationship>(RelationshipItemIOBurner(
                className,
                fuelClass,
                fuelAmount,
                burnTime
        ))


        supplementalResource?.let {
            rel.add(RelationshipItemIO(
                    className,
                    it,
                    supplementalResourceAmount!!
            ))
        }
        rel
    }.flatten()
}

@Component
class PowerGeneratorFuelConsumesFuel : SatisfactoryRelationshipTransformer<FGBuildableGeneratorFuel, Collection<Relationship>>(
        FGBuildableGeneratorFuel::class,
        PowerGeneratorEntity::class to ItemDescriptorEntity::class,
        "CONSUMED_BY"
) {
    @Autowired
    override lateinit var neo4jClient: Neo4jClient

    override var relationships: List<Relationship> = mutableListOf()

    override fun transform(transformIn: FGBuildableGeneratorFuel): Collection<Relationship> {
        return transformFuel(transformIn.ClassName, transformIn.mFuel)
    }
}
@Component
class PowerGeneratorFuelConsumesNuclear : SatisfactoryRelationshipTransformer<FGBuildableGeneratorNuclear, Collection<Relationship>>(
        FGBuildableGeneratorNuclear::class,
        PowerGeneratorEntity::class to ItemDescriptorEntity::class,
        "CONSUMED_BY"
) {
    @Autowired
    override lateinit var neo4jClient: Neo4jClient

    override var relationships: List<Relationship> = mutableListOf()

    override fun transform(transformIn: FGBuildableGeneratorNuclear): Collection<Relationship> {
        return transformFuel(transformIn.ClassName, transformIn.mFuel)
    }
}

