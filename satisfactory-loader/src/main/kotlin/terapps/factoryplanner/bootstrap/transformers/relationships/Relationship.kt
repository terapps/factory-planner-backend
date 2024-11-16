package terapps.factoryplanner.bootstrap.transformers.relationships

open class Relationship(val sourceId: String,
                        val destinationId: String) {
    override fun hashCode(): Int {
        return sourceId.hashCode() + destinationId.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Relationship) return false

        return hashCode() == other.hashCode()
    }
}

class RelationshipItemIO(
        sourceId: String,
        destinationId: String,
        val outputPerCycle: Float
) : Relationship(sourceId, destinationId) {
    override fun hashCode(): Int {
        return super.hashCode() + outputPerCycle.hashCode()
    }
}

class RelationshipWeight(
        sourceId: String,
        destinationId: String,
        val outputPerCycle: Float
) : Relationship(sourceId, destinationId) {
    override fun hashCode(): Int {
        return super.hashCode() + outputPerCycle.hashCode()
    }
}