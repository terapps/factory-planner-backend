package terapps.factoryplanner.bootstrap.transformers.entities

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.generated.FGSchematic
import terapps.factoryplanner.bootstrap.extractListEntry
import terapps.factoryplanner.bootstrap.toBoolean
import terapps.factoryplanner.bootstrap.transformers.AbstractTransformer
import terapps.factoryplanner.core.entities.Schematic
import terapps.factoryplanner.core.entities.SchematicDependency
import terapps.factoryplanner.core.repositories.SchematicRepository

@Component
class SchematicTransformer : AbstractTransformer<FGSchematic, Schematic>(FGSchematic::class) {
    @Autowired
    private lateinit var schematicRepository: SchematicRepository

    override fun transform(transformIn: FGSchematic): Schematic {
        val dependencies = transformIn.mSchematicDependencies.map {

            SchematicDependency(
                    it["Class"] as String,
                    it["mRequireAllSchematicsToBePurchased"].toString().toBoolean(),
                    (it["mSchematics"] as? String)?.extractListEntry()?.map {
                        val blueprintClassRegex = ".*BlueprintGeneratedClass'.*\\.(?<captured>.*)'".toRegex()
                        val descriptor = blueprintClassRegex.find(it)?.groups?.get("captured")?.value ?: throw Error("ItemIO: doesnt match regex $it")

                        Schematic(className = descriptor)
                    }?.toSet() ?: emptySet()
            )
        }.toSet()

        return Schematic(
                className = transformIn.ClassName,
                type = transformIn.mType,
                displayName = transformIn.mDisplayName,
                description = transformIn.mDescription,
                tier = transformIn.mTechTier,
                timeToComplete = transformIn.mTimeToComplete,
                depdendsOn = dependencies
        )
    }

    override fun save(output: Schematic): Schematic = schematicRepository.save(output)
}