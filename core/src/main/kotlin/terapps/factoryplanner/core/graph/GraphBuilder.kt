package terapps.factoryplanner.core.graph

interface GraphEdge {
    val source: String
    val target: String
}


class GraphBuilder<Node, Edge : GraphEdge>(
        val nodes: MutableSet<Node> = mutableSetOf(),
        val edges: MutableSet<Edge> = mutableSetOf()
) {
    fun addNode(node: Node) {
        nodes.add(node)
    }

    fun addEdge(edge: Edge) {
        edges.add(edge)
    }

    fun getOutgoingEdges(node: Node): Collection<Edge> {
        return edges.filter {
            it.source == node
        }
    }

    fun getIncomingEdges(node: Node): Collection<Edge> {
        return edges.filter {
            it.target == node
        }
    }
}