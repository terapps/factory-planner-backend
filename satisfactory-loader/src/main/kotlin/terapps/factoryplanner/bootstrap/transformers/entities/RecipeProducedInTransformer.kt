package terapps.factoryplanner.bootstrap.transformers.entities

import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.ItemRef
import terapps.factoryplanner.bootstrap.dto.generated.FGRecipe
import terapps.factoryplanner.bootstrap.extractListEntry
import terapps.factoryplanner.bootstrap.transformers.Transformer
import kotlin.reflect.KClass

@Component
class RecipeProducedInTransformer : Transformer<FGRecipe, List<ItemRef>> {
    override fun transform(transformIn: FGRecipe): List<ItemRef> {

        return transformIn.mProducedIn.extractListEntry().map {
            when (val descriptor = ".*\\.(.*)".toRegex().matchEntire(it)!!.groupValues[1]) {
                "BP_BuildGun_C",
                "FGBuildGun",
                "Build_AutomatedWorkBench_C",
                "BP_WorkBenchComponent_C",
                "FGBuildableAutomatedWorkBench",
                "BP_WorkshopComponent_C" -> "manual_crafting"

                else -> descriptor
            }

        }
    }

    override fun supportsClass(clazz: KClass<*>): Boolean {
        return false
    }
}
