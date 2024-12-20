package terapps.factoryplanner.bootstrap.steps.components

import org.slf4j.LoggerFactory
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
import terapps.factoryplanner.core.repositories.ExtractorRepository
import terapps.factoryplanner.core.repositories.ItemDescriptorRepository

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

    override val priority: Int
        get() = 2


    override fun prepare() {
        val items = itemDescriptorRepository.findAll()
        val extractors = extractorRepository.findAll()
        val config = supportedCategories { category, transformer ->
            logger.info("Loading mapping category ${category.classType.simpleName} -> ${transformer.javaClass.simpleName}")

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
            logger.info("Binding ${it.relationships.size} relationships for ${it.relationshipName}")
            if (it.relationships.isNotEmpty()) {
                it.runCypherQuery()
            }
        }
    }

    override fun dispose() {


    }
}