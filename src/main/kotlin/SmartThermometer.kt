interface SmartThermometerProtocol : SmartDevice {
    var temperature: Int?
}

class SmartThermometerBase(
    override val id: String,
    override val name: String,
    override val location: Location,
    override var enabled: Boolean,
    override var temperature: Int?,
) : SmartThermometerProtocol

class SmartThermometerStateImpl(private val smartThermometer: SmartThermometerProtocol) : State {
    override fun toStateString(): String = """
        Name = ${smartThermometer.name}
        Location = ${smartThermometer.location}
        ...
        """.trimIndent()

}

fun SmartThermometer(
    id: String,
    name: String,
    location: Location,
    enabled: Boolean = false,
    temperature: Int? = null
): SmartThermometer {
    val c = SmartThermometerBase(id, name, location, enabled, temperature)
    return SmartThermometer(c, SmartThermometerStateImpl(c))
}

class SmartThermometer(
    private val smartThermometerBase: SmartThermometerBase,
    private val state: State
) : SmartThermometerProtocol by smartThermometerBase, State by state {

    override fun toString(): String {
        return toStateString()
    }

}