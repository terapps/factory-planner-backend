package terapps.factoryplanner.bootstrap.steps.components

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import terapps.factoryplanner.bootstrap.dto.SatisfactoryStaticData
import terapps.factoryplanner.bootstrap.steps.RootStep
import terapps.factoryplanner.bootstrap.transformers.SatisfactoryRelationshipTransformer
import terapps.factoryplanner.bootstrap.transformers.TransformerOrchestrator
import terapps.factoryplanner.bootstrap.transformers.relationships.ItemExtractedIn
import terapps.factoryplanner.bootstrap.transformers.relationships.RecipeManufacturedIn
import terapps.factoryplanner.bootstrap.transformers.relationships.Relationship
import terapps.factoryplanner.core.entities.ExtractorRepository
import terapps.factoryplanner.core.entities.ItemDescriptorRepository

@Component
class RelationshipStep : RootStep,  TransformerOrchestrator<SatisfactoryRelationshipTransformer<Any, Any>>(SatisfactoryRelationshipTransformer::class) {
    @Autowired
    override lateinit var satisfactoryStaticData: SatisfactoryStaticData

    @Autowired
    override lateinit var applicationContext: ApplicationContext

    @Autowired
    private lateinit var itemDescriptorRepository: ItemDescriptorRepository

    @Autowired
    private lateinit var extractorRepository: ExtractorRepository


    override fun prepare() {
        val items = itemDescriptorRepository.findAll()
        val extractors = extractorRepository.findAll()
        val config = supportedCategories { category, transformer ->
            println("[${this.javaClass.simpleName}] Loading mapping category ${category.classType.simpleName} -> ${transformer.javaClass.simpleName}")

            if (transformer is ItemExtractedIn) {
                transformer.items = items
                transformer.extractors = extractors
            }
            if (transformer is RecipeManufacturedIn) {
                transformer.items = items
            }
        }

        run(config) { gameEntity, transformer ->
           transformer.relationships += transformer.transform(gameEntity) as List<Relationship>
        }
        val transformers = config.values.flatten().distinctBy { it.javaClass.name }

        transformers.forEach {
            it.runCypherQuery()
        }
    }

    override fun dispose() {


    }
}