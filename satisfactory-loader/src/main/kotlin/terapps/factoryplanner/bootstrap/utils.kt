package terapps.factoryplanner.bootstrap


fun String.extractListEntry() = replace("(", "").replace(")", "").split(",")
fun String.extractDictEntry(): MutableList<Map<String, String>> {
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