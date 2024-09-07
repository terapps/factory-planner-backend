package terapps.factoryplanner.bootstrap.transformers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import terapps.factoryplanner.core.entities.ItemDescriptor
import terapps.factoryplanner.core.entities.ItemDescriptorProducedBy
import terapps.factoryplanner.core.entities.ItemDescriptorRepository
import terapps.factoryplanner.core.entities.Recipe
import kotlin.jvm.optionals.getOrElse
import kotlin.reflect.KClass

@Component
class ItemDescriptorProducedByTransformer : SatisfactoryTransformer<Pair<Recipe, ItemIO>, ItemDescriptor> {
    @Autowired
    lateinit var itemDescriptorRepository: ItemDescriptorRepository

    override fun transform(transformIn: Pair<Recipe, ItemIO>): ItemDescriptor {
        val (recipe, input) = transformIn

        return input.toItemIO { itemClass, out ->
            itemDescriptorRepository.findById(itemClass)
                    .getOrElse { throw Error("Cannot transform ${itemClass}") }
                    .also {
                        it.producedBy += ItemDescriptorProducedBy(
                                recipe,
                                if (it.form == "RF_LIQUID") out / 1000f else out
                        )
                    }
        }
    }

    override fun save(output: ItemDescriptor): ItemDescriptor = itemDescriptorRepository.save(output)
    override fun supportsClass(clazz: KClass<*>): Boolean {
        TODO("Not implemented")
    }

}
