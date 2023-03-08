interface SmartLightProtocol : SmartDevice {
    fun switchOn()

    fun switchOff()

    fun switch()

}

class SmartLightBase(
    override val id: String,
    override val name: String,
    override val location: Location,
    override var enabled: Boolean,
) : SmartLightProtocol {
    
    override fun switchOn() {
        enabled = true
    }

    override fun switchOff() {
        enabled = false
    }

    override fun switch() {
        enabled = enabled xor true
    }
    
}

class SmartLightStateImpl(private val smartLight: SmartLightProtocol) : State {
    override fun toStateString(): String = """
        Name = ${smartLight.name}
        Location = ${smartLight.location}
        ...
        """.trimIndent()

}

fun SmartLight(
    id: String,
    name: String,
    location: Location,
    enabled: Boolean = false,
): SmartLight {
    val c = SmartLightBase(id, name, location, enabled)
    return SmartLight(c, SmartLightStateImpl(c))
}

class SmartLight(
    private val smartLightBase: SmartLightBase,
    private val state: State
) : SmartLightProtocol by smartLightBase, State by state {

    override fun toString(): String {
        return toStateString()
    }

}