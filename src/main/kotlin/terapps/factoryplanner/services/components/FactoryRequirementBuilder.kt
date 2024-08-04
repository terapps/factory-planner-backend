package terapps.factoryplanner.services.components

import terapps.factoryplanner.entities.satisfactory.ItemDescriptor
import terapps.factoryplanner.entities.satisfactory.ItemDescriptorRepository
import terapps.factoryplanner.entities.satisfactory.Recipe
import terapps.factoryplanner.entities.satisfactory.RecipeRepository
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

    private fun addRequirement(factorySite: FactorySite): FactoryRequirementBuilder {
        // TODO proposition on byproducts
        println("adding descriptor ${factorySite.targetDescriptor.id}")
        // TODO extractof stuff / overclocking

        if (factorySite.extractor != null) {
            // Dont go in ingredients to avoid infinite loop caused by raw resources recipes producing the same item than ingredient
            return this
        }

        factorySite.targetRecipe.ingredients.forEach { itemIO ->
            val ingredientRequirementsPerCycle = factorySite.numberOfMachines!! * itemIO.outputPerCycle
            val ingredientRequirementsPerMinute = ingredientRequirementsPerCycle * (60f / factorySite.targetRecipe.manufacturingDuration)

            println("Adding ingredient [${itemIO.descriptor}] with $ingredientRequirementsPerMinute")
            addRequirement(itemIO.descriptor, ingredientRequirementsPerMinute, factorySite)
        }

        return this
    }

    private fun addRequirement(targetDescriptor: ItemDescriptor, targetRecipe: Recipe, amountPerMinute: Float, parent: FactorySite? = null): FactoryRequirementBuilder {
        val existingRequirement = requirements.firstOrNull { it.targetDescriptor.id == targetDescriptor.id }
        val factorySite = FactorySite(targetDescriptor, targetRecipe, craftingMachine = targetRecipe.producedIn, extractor = targetRecipe.extractedIn.firstOrNull()).apply {
            this.targetAmountPerMinute = amountPerMinute
            if (parent != null) {
                this.destinations.add(parent)
            }
        }

        if (existingRequirement != null) {
            println("Found existing requirement ${existingRequirement.targetDescriptor.id}, adding ${factorySite.targetAmountPerMinute}")
            existingRequirement.targetAmountPerMinute += factorySite.targetAmountPerMinute
            existingRequirement.destinations.add(factorySite)
        } else {
            requirements.add(factorySite)
        }
        return addRequirement(factorySite)
    }

    private fun addRequirement(descriptor: ItemDescriptor, amount: Float, parent: FactorySite? = null): FactoryRequirementBuilder {
        val recipe = getRecipe(descriptor)

        println("Resolved Recipe [${recipe.id}] for descriptor [${descriptor.id}]")
        return addRequirement(descriptor, recipe, amount, parent)
    }

    fun addRequirement(descriptorClass: String, amount: Float, parent: FactorySite? = null): FactoryRequirementBuilder {
        val descriptor = itemDescriptorRepository.findById(descriptorClass).getOrNull()
                ?: throw Error("Descriptor not found $descriptorClass")

        // TODO Find the right extractor with game profile (unlocks / most optimized)
        return addRequirement(descriptor, amount, parent)
    }

    private fun getRecipe(descriptor: ItemDescriptor): Recipe {
        val recipeClassNames = this.recipeRepository.findRecipeIdsByItemDescriptorId(descriptor.id).takeIf { it.isNotEmpty() }
                ?: throw Error("No recipe classes for ${descriptor.id}")
        val recipes = this.recipeRepository.findByIdIn(recipeClassNames).takeIf { it.isNotEmpty() }
                ?: throw Error("No recipe for classes ${recipeClassNames}")
        val recipe = recipes.firstOrNull { it.id.contains("Recipe_ResidualPlastic_C") } ?: recipes.first()

        return recipe
    }

    fun build() = this.requirements
}