package terapps.factoryplanner.bootstrap.transformers.entities

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.generated.FGSchematic
import terapps.factoryplanner.bootstrap.extractListEntry
import terapps.factoryplanner.bootstrap.toBoolean
import terapps.factoryplanner.bootstrap.transformers.AbstractTransformer
import terapps.factoryplanner.bootstrap.transformers.BatchList
import terapps.factoryplanner.core.entities.SchematicEntity
import terapps.factoryplanner.core.entities.SchematicDependencyEntity
import terapps.factoryplanner.core.repositories.SchematicRepository

@Component
class SchematicTransformer : AbstractTransformer<FGSchematic, SchematicEntity>(FGSchematic::class) {
    @Autowired
    private lateinit var schematicRepository: SchematicRepository

    override val batch: BatchList<SchematicEntity> = BatchList() {
        schematicRepository.saveAll(it)
    }

    override fun transform(transformIn: FGSchematic): SchematicEntity {
        val dependencies = transformIn.mSchematicDependencies.map {

            SchematicDependencyEntity(
                    it["Class"] as String,
                    it["mRequireAllSchematicsToBePurchased"].toString().toBoolean(),
                    (it["mSchematics"] as? String)?.extractListEntry()?.map {
                        val blueprintClassRegex = ".*BlueprintGeneratedClass'.*\\.(?<captured>.*)'".toRegex()
                        val descriptor = blueprintClassRegex.find(it)?.groups?.get("captured")?.value ?: throw Error("ItemIO: doesnt match regex $it")

                        SchematicEntity(className = descriptor)
                    }?.toSet() ?: emptySet()
            )
        }.toSet()

        return SchematicEntity(
                className = transformIn.ClassName,
                type = transformIn.mType,
                displayName = transformIn.mDisplayName,
                description = transformIn.mDescription,
                tier = transformIn.mTechTier,
                timeToComplete = transformIn.mTimeToComplete,
                depdendsOn = dependencies
        )
    }
}