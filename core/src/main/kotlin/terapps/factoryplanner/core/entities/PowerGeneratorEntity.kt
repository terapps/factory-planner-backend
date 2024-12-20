package terapps.factoryplanner.core.entities

import org.springframework.data.neo4j.core.schema.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship

@Node("PowerGenerator")
class PowerGeneratorEntity(
        @Id
        val className: String,
        val displayName: String,
        val description: String,
        val currentPotential: Double,
        val fuelLoadAmount: Double,
        val powerProduction: Double,
        val minPotential: Double,
        val maxPotential: Double,
        @Relationship(type = "CONSUMED_BY", direction = Relationship.Direction.INCOMING)
        val fuel: PowerGeneratorFuelRequires? = null,
        @Relationship(type = "CONSUMED_BY", direction = Relationship.Direction.INCOMING)
        val supplementalResource: PowerGeneratorRequires? = null,
        @Relationship(type = "PRODUCED_BY", direction = Relationship.Direction.OUTGOING)
        val byproducts: PowerGeneratorProduces? = null,
)
