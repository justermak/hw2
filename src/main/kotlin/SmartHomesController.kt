class SmartHomesController {
    private val smartHomes: MutableList<SmartHome> = mutableListOf()

    fun register(smartHome: SmartHome) {
        smartHomes.add(smartHome)
    }

    fun unregister(smartHome: SmartHome) {
        smartHomes.remove(smartHome)
    }

    fun getHomeById(id: String): SmartHome? = smartHomes.firstOrNull { it.id == id }

    fun getHomes(): List<SmartHome> = smartHomes
}
