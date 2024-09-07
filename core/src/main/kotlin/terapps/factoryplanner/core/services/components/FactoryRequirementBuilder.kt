package terapps.factoryplanner.core.services.components

import terapps.factoryplanner.core.OptimizationCriterion
import terapps.factoryplanner.core.entities.ItemDescriptor
import terapps.factoryplanner.core.entities.ItemDescriptorRepository
import terapps.factoryplanner.core.entities.Recipe
import terapps.factoryplanner.core.entities.RecipeRepository
import terapps.factoryplanner.core.OptimizationCriterion.*
import kotlin.jvm.optionals.getOrNull

class FactoryRequirementBuilder(
        private val recipeRepository: RecipeRepository,
        private val itemDescriptorRepository: ItemDescriptorRepository,
        private val requirements: MutableList<FactorySite>
) {

    companion object {
        fun newBuilder(
                recipeRepository: RecipeRepository,
                itemDescriptorRepository: ItemDescriptorRepository
        ) = FactoryRequirementBuilder(recipeRepository, itemDescriptorRepository, mutableListOf())
    }
/*
    private fun addRequirement(factorySite: FactorySite): FactoryRequirementBuilder {
        if (factorySite.extractor != null) {
            // Dont go in ingredients to avoid infinite loop caused by raw resources recipes producing the same item than ingredient
            return this
        }

        factorySite.targetRecipe.ingredients.forEach { itemIO ->
            val ingredientRequirementsPerCycle = factorySite.numberOfMachines!! * itemIO.outputPerCycle
            val ingredientRequirementsPerMinute = ingredientRequirementsPerCycle * (60f / factorySite.targetRecipe.manufacturingDuration)

            addRequirement(itemIO.descriptor, ingredientRequirementsPerMinute, factorySite)
        }

        return this
    }

    fun addRequirement(targetDescriptor: ItemDescriptor, targetRecipe: Recipe, amountPerMinute: Float, parent: FactorySite? = null): FactoryRequirementBuilder {
        val existingRequirement = requirements.firstOrNull { it.targetDescriptor.id == targetDescriptor.id }
        val factorySite = FactorySite(targetDescriptor, targetRecipe, craftingMachine = targetRecipe.producedIn, targetAmountPerMinute = amountPerMinute, extractor = targetRecipe.extractedIn.firstOrNull()).apply {
            if (parent != null) {
                this.destinations.add(parent)
            }
        }

        if (existingRequirement != null) {
            existingRequirement.destinations.add(existingRequirement.plus(factorySite.targetAmountPerMinute))
        } else {
            requirements.add(factorySite)
        }
        return addRequirement(factorySite)
    }

    private fun addRequirement(descriptor: ItemDescriptor, amount: Float, parent: FactorySite? = null): FactoryRequirementBuilder {
        // TODO Default to mat weight, add optimization profile from user
        val recipe = getRecipe(descriptor, RAW_MATERIAL_WEIGHT)

        return addRequirement(descriptor, recipe, amount, parent)
    }

    fun addRequirement(descriptorClass: String, amount: Float, parent: FactorySite? = null): FactoryRequirementBuilder {
        val descriptor = itemDescriptorRepository.findById(descriptorClass).getOrNull()
                ?: throw Error("Descriptor not found $descriptorClass")

        // TODO Find the right extractor with game profile (unlocks / most optimized)
        return addRequirement(descriptor, amount, parent)
    }

    private fun getRecipe(descriptor: ItemDescriptor, optimizationCriterion: OptimizationCriterion): Recipe {

        throw Error("There is a recipe ${descriptor}")
*//*
        return when (optimizationCriterion) {
            RAW_MATERIAL_WEIGHT -> descriptor.producedBy.minBy { it.weightedPoints }
            POWER_CONSUMPTION -> throw NotImplementedError("Power consumption not impl")
            BUILDING_NUMBER -> descriptor.producedBy.minBy { it.buildingCountPoints }
            SINK_VALUE -> descriptor.producedBy.minBy { it.sinkingPoints }
        }
*//*
    }

    fun build() = this.requirements*/
}