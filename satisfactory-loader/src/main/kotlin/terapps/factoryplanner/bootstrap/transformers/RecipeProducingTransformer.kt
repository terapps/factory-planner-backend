package terapps.factoryplanner.bootstrap.transformers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.core.entities.*
import kotlin.jvm.optionals.getOrElse
import kotlin.reflect.KClass

@Component
class RecipeProducingTransformer : SatisfactoryTransformer<Pair<Recipe, ItemIO>, RecipeProducing> {
    @Autowired
    lateinit var itemDescriptorRepository: ItemDescriptorRepository

    override fun transform(transformIn: Pair<Recipe, ItemIO>): RecipeProducing {
        val (_, input) = transformIn

        return input.toItemIO { itemClass, out ->
            itemDescriptorRepository.findById(itemClass)
                    .getOrElse { throw Error("Cannot transform ${itemClass}") }
                    .run {
                        RecipeProducing(
                                this,
                                if (form == "RF_LIQUID") out / 1000f else out
                        )
                    }
        }
    }

    override fun save(output: RecipeProducing): RecipeProducing = TODO("Not implemented")
    override fun supportsClass(clazz: KClass<*>): Boolean {
        TODO("Not implemented")
    }

}
