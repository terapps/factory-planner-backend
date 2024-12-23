package terapps.factoryplanner.core.services.components

import org.neo4j.driver.summary.ResultSummary
import org.springframework.data.neo4j.core.Neo4jClient
import kotlin.reflect.full.memberProperties


abstract class RelationshipQuery(
        val relationship: Pair<String, String>,
        val relationshipName: String
) {
    protected abstract var neo4jClient: Neo4jClient

    fun runCypherQuery(): ResultSummary {
        return neo4jClient.query(cypherQuery)
                .bind(bindRelationships).to(parameter)
                .run()
    }

    protected val cypherQuery: String
        get() {
            return """
        UNWIND $$parameter AS relationship
            MATCH (p:${relationship.first} {$sourceIdProp: relationship.${sourceIdParam}}), (m:${relationship.second} {$destIdProp: relationship.${destinationIdParam}})
            CREATE (p)-[:${relationshipName} ${relationshipProperties}]->(m)
    """.trimIndent()
        }

    abstract var relationships: List<Relationship>

    protected open val sourceIdProp: String = "className"
    protected open val destIdProp: String = "className"

    private val sourceIdParam: String
        get() = Relationship::sourceId.name

    private val destinationIdParam: String
        get() = Relationship::destinationId.name

    private val bindRelationships
        get() = relationships.distinct().map {
        it.toMap()
    }

    private val parameter: String
        get() {
            return RelationshipQuery::relationships.name
        }

    protected open val relationshipProperties: String
        get() {
            val firstElem = bindRelationships.firstOrNull() ?: throw Error("No relationship ${this.javaClass.simpleName} - ${relationshipName}")
            val keys = firstElem.keys.filterNot { it in Relationship::class.memberProperties.map { it.name } }

            return if (keys.isEmpty()) "" else "{ ${keys.joinToString(", ") { "$it: relationship.$it" }} }"
        }
}