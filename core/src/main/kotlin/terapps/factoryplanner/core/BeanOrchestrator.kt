package terapps.factoryplanner.core

import org.springframework.context.ApplicationContext
import kotlin.reflect.KClass

abstract class BeanOrchestrator<T: Any>(private val beanType: KClass<*>) {
    protected abstract var applicationContext: ApplicationContext

    protected val beans: MutableCollection<T>
        get() {
            return applicationContext.getBeansOfType(beanType.java).values as MutableCollection<T>
        }

    protected abstract fun resolveBean(clazz: KClass<*>): T?
    protected abstract fun resolveBeans(clazz: KClass<*>): Collection<T>
}