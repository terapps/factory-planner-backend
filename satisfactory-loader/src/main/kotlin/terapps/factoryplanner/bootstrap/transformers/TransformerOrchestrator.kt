package terapps.factoryplanner.bootstrap.transformers

import terapps.factoryplanner.bootstrap.dto.GameObjectCategory
import terapps.factoryplanner.bootstrap.dto.SatisfactoryStaticData
import terapps.factoryplanner.core.BeanOrchestrator
import kotlin.reflect.KClass

abstract class TransformerOrchestrator<T: Transformer<Any, Any>>(clazz: KClass<*>): BeanOrchestrator<T>(clazz) {
    abstract var satisfactoryStaticData: SatisfactoryStaticData

    override fun resolveBean(clazz: KClass<*>): T? {
        val filtered = resolveBeans(clazz)

        if (filtered.size > 1) {
            throw Error("More than one bean supporting ${clazz}")
        }
        return filtered.firstOrNull()
    }

    override fun resolveBeans(clazz: KClass<*>): Collection<T> = beans.filter { it.supportsClass(clazz) }


    protected fun supportedCategories(onload: (category: GameObjectCategory<*>, transformer: T) -> Unit): Map<GameObjectCategory<*>, Collection<T>> {
        return satisfactoryStaticData.filter { it.classType.isSupportedCategory() }.associateWith {category ->
            resolveBeans(category.classType).onEach { onload(category, it) }
        }
    }

    protected fun run(runConfig: Map<GameObjectCategory<*>, Collection<T>>, runner: (Any, T) -> Unit) =
        runConfig.forEach { (category, transformers) ->
            category.Classes.forEach { gameEntity ->
                transformers.forEach { transformer ->
                    runner(gameEntity, transformer)
                }
            }
        }

    private fun KClass<*>.isSupportedCategory(): Boolean {
        return resolveBeans(this).isNotEmpty()
    }

}