package terapps.factoryplanner.bootstrap.transformers

import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.generated.FGRecipe
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

@Component
class RecipeProducedInTransformer : SatisfactoryTransformer<FGRecipe, List<ItemRef>> {
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


    override fun save(output: List<ItemRef>): List<ItemRef> {
        TODO("Not implemented: Should not be called standalone")
    }

    override fun supportsClass(clazz: KClass<*>): Boolean {
        return FGRecipe::class.isSubclassOf(clazz)
    }
}
