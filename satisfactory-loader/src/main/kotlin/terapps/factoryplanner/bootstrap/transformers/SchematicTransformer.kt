package terapps.factoryplanner.bootstrap.transformers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.generated.FGSchematic
import terapps.factoryplanner.core.entities.Schematic
import terapps.factoryplanner.core.entities.SchematicRepository

@Component
class SchematicTransformer : AbstractTransformer<FGSchematic, Schematic>(FGSchematic::class) {
    @Autowired
    private lateinit var schematicRepository: SchematicRepository

    override fun transform(transformIn: FGSchematic): Schematic {
        return Schematic(
                className = transformIn.ClassName,
                type = transformIn.mType,
                displayName = transformIn.mDisplayName,
                description = transformIn.mDescription,
                tier = transformIn.mTechTier,
                timeToComplete = transformIn.mTimeToComplete,
        )
    }

    override fun save(output: Schematic): Schematic = schematicRepository.save(output)
}