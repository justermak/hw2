interface SmartHomeProtocol {
    val id: String
    val name: String
    val devices: MutableList<SmartDevice>

    fun register(d: SmartDevice)

    fun unregister(d: SmartDevice)

    fun getDeviceById(id: String): SmartDevice?

    fun getDevicesByLocation(location: Location): List<SmartDevice>
}

class SmartHomeBase(
    override val id: String,
    override val name: String,
    override val devices: MutableList<SmartDevice>,
) : SmartHomeProtocol {

    override fun register(d: SmartDevice) {
        if (d is SmartThermometer && devices.filterIsInstance<SmartThermometer>().any { it.location == d.location }) {
            throw Exception("There can only be one thermometer in one location")
        } else {
            devices.add(d)
        }
    }

    override fun unregister(d: SmartDevice) {
        devices.remove(d)
    }

    override fun getDeviceById(id: String): SmartDevice? = devices.firstOrNull { it.id == id }

    override fun getDevicesByLocation(location: Location): List<SmartDevice> = devices.filter { it.location == location }
}

class SmartHomeStateImpl(private val smartHome: SmartHomeProtocol) : State {
    override fun toStateString(): String = """
        Name = ${smartHome.name}
        Devices = ${smartHome.devices.map { it.name }}
    """.trimIndent()
}

fun SmartHome(
    id: String,
    name: String,
    devices: MutableList<SmartDevice> = mutableListOf(),
): SmartHome {
    val c = SmartHomeBase(id, name, devices)
    return SmartHome(c, SmartHomeStateImpl(c))
}

class SmartHome(
    private val smartHomeBase: SmartHomeBase,
    private val state: State,
) : SmartHomeProtocol by smartHomeBase, State by state {
    override fun toString(): String {
        return toStateString()
    }
}
