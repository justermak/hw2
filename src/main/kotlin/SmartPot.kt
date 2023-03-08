const val EPS : Double = 1e-6

interface SmartPotProtocol : SmartDevice {
    var temperature: Int?
    var waterVolume: Double
    var isWaterBoiled: Boolean
    val minimumWaterLevel : Double
    val maximumWaterLevel : Double

    fun boilWater()

    fun chooseTemperature(newTemperature: Int)

}

class SmartPotBase(
    override val id: String,
    override val name: String,
    override val location: Location,
    override var enabled: Boolean,
    override var temperature: Int?,
    waterVolume: Double = 0.0,
    override var isWaterBoiled: Boolean,
    override val minimumWaterLevel: Double = 1.0,
    override val maximumWaterLevel: Double = 10.0

) : SmartPotProtocol {

    override var waterVolume : Double = 0.0
        set(value) {
            if (value < -EPS || value > maximumWaterLevel - EPS) {
                throw Exception("Incorrect amount of water poured")
            }
            this.isWaterBoiled = false
            field = value
        }
    init {
        this.waterVolume = waterVolume
    }
    override fun boilWater() {
        if (waterVolume < minimumWaterLevel + EPS) {
            throw Exception("No water to boil")
        }
        isWaterBoiled = true
    }

    override fun chooseTemperature(newTemperature: Int) {
        temperature = newTemperature
    }

}

class SmartPotStateImpl(private val smartPot: SmartPotProtocol) : State {
    override fun toStateString(): String = """
        Name = ${smartPot.name}
        Location = ${smartPot.location}
        ...
        """.trimIndent()

}

fun SmartPot(
    id: String,
    name: String,
    location: Location,
    enabled: Boolean = false,
    temperature: Int? = null,
    waterVolume: Double = 0.0,
    isWaterBoiled: Boolean = true,
    minimumWaterLevel: Double = 1.0,
    maximumWaterLevel: Double = 10.0
): SmartPot {
    val c = SmartPotBase(id, name, location, enabled, temperature, waterVolume, isWaterBoiled, minimumWaterLevel, maximumWaterLevel)
    return SmartPot(c, SmartPotStateImpl(c))
}

class SmartPot(
    private val smartPotBase: SmartPotBase,
    private val state: State
) : SmartPotProtocol by smartPotBase, State by state {

    override fun toString(): String {
        return toStateString()
    }


}