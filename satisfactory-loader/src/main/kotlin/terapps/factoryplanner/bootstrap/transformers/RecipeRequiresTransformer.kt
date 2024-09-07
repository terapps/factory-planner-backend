package terapps.factoryplanner.bootstrap.transformers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.core.entities.ItemDescriptorRepository
import terapps.factoryplanner.core.entities.RecipeRequires
import kotlin.jvm.optionals.getOrElse


@Component
class RecipeRequiresTransformer : SatisfactoryTransformer<
        ItemIO, RecipeRequires> {
    @Autowired
    lateinit var itemDescriptorRepository: ItemDescriptorRepository

    override fun transform(transformIn: ItemIO): RecipeRequires {
        return transformIn.toItemIO { itemClass, out ->
            val descriptor = itemDescriptorRepository.findById(itemClass).getOrElse { throw Error("Cannot transform ${itemClass}") }

            RecipeRequires(
                    descriptor,
                    if (descriptor.form == "RF_LIQUID") out / 1000f else out
            )
        }

    }

    override fun save(output: RecipeRequires): RecipeRequires {
        TODO("Not implemented: Should not be called standalone")
    }
}
