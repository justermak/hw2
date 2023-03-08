interface SmartTvProtocol : SmartDevice {
    var channel: String?
    var volume: Int?

    fun changeChannel(newChannel: String)

    fun changeVolume(newVolume: Int)

}

class SmartTvBase(
    override val id: String,
    override val name: String,
    override val location: Location,
    override var enabled: Boolean,
    override var channel: String?,
    override var volume: Int?,
) : SmartTvProtocol {

    override fun changeChannel(newChannel: String) {
        channel = newChannel
    }

    override fun changeVolume(newVolume: Int) {
        if (newVolume < 0 || newVolume > 100) {
            throw Exception("Incorrect volume")
        }
        volume = newVolume
    }

}

class SmartTvStateImpl(private val smartTv: SmartTvProtocol) : State {
    override fun toStateString(): String = """
        Name = ${smartTv.name}
        Location = ${smartTv.location}
        ...
        """.trimIndent()

}

fun SmartTv(
    id: String,
    name: String,
    location: Location,
    enabled: Boolean = false,
    channel: String? = null,
    volume: Int? = null
): SmartTv {
    val c = SmartTvBase(id, name, location, enabled, channel, volume)
    return SmartTv(c, SmartTvStateImpl(c))
}

class SmartTv(
    private val smartTvBase: SmartTvBase,
    private val state: State
) : SmartTvProtocol by smartTvBase, State by state {

    override fun toString(): String {
        return toStateString()
    }

}