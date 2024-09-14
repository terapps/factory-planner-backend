package terapps.factoryplanner.bootstrap.transformers

typealias ItemIO = Map<String, String>
typealias ItemRef = String
fun String.extractListEntry(): List<ItemRef> = replace("\"", "").replace("(", "").replace(")", "").split(",").filterNot { it.isEmpty() }
fun String.extractDictEntry(): MutableList<ItemIO> {
    val result = mutableListOf<Map<String, String>>()

    if (isEmpty()) {
        return result
    }
    val outerPairs = this.substring(1, this.length - 1).split("),(")

    for (outerPair in outerPairs) {
        val map = mutableMapOf<String, String>()
        val innerPairs = outerPair.split(",")

        for (innerPair in innerPairs) {
            val (key, value) = innerPair.replace("(", "").replace(")", "").split("=")
            map[key] = value
        }

        result.add(map)
    }

    return result
}
fun <T> ItemIO.toItemIO(transform: (String, Float) -> T): T {
    val blueprintClassRegex = ".*BlueprintGeneratedClass'.*\\.(?<captured>.*)'".toRegex()
    val itemClass = this["ItemClass"] ?: throw Error("ItemIO: itemclass not found $this")

    val descriptor = blueprintClassRegex.find(itemClass)?.groups?.get("captured")?.value ?: throw Error("ItemIO: doesnt match regex $itemClass")
    val out = this["Amount"]!!.toFloat()

    return transform(descriptor, out)
}