package terapps.factoryplanner.core.configurations

import org.modelmapper.Converter
import org.modelmapper.ModelMapper
import org.modelmapper.Provider
import org.modelmapper.TypeMap
import org.modelmapper.convention.MatchingStrategies
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import terapps.factoryplanner.core.dto.*
import terapps.factoryplanner.core.projections.*
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType


@Configuration
class ModelMapperConfiguration {
    companion object {
        private val typemap = mapOf(
                ItemDescriptorSummary::class to  ItemDescriptorDto::class,
                RecipeSummary::class to RecipeDto::class,
                RecipeRequiringSummary::class to  RecipeRequiringDto::class,
                RecipeProducingSummary::class to  RecipeProducingDto::class,
        )

        fun ModelMapper.createTypeMap(src: KClass<*>, dest: KClass<*>): TypeMap<out Any, out Any> = createTypeMap(
                src.java,
                dest.java
        ).apply {
            val ctor = dest.constructors.firstOrNull { it.parameters.firstOrNull()?.type?.isSubtypeOf(src.starProjectedType) == true }
                    ?: throw Error("Cannot find ctor with $src in $dest")

            provider = Provider {
                ctor.call(it.source)
            }
        }

    }

    @Bean
    fun modelMapper(): ModelMapper = ModelMapper().apply {
        configuration.matchingStrategy = MatchingStrategies.STRICT

        typemap.forEach { (src, dest) ->
            createTypeMap(src, dest)
        }
    }
}