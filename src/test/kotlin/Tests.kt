import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.assertFails

class Tests {

    @BeforeEach
    fun setUp() {
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun smartTvTests() {
        val x = SmartTv("First", "myFav", Location.KITCHEN)
        x.changeChannel("BBC")
        assertEquals(x.channel, "BBC")
        x.changeVolume(80)
        assertEquals(x.volume, 80)
        assertFails { x.changeVolume(120) }
    }

    @Test
    fun smartLightTests() {
        val x = SmartLight("First", "myFav", Location.KITCHEN)
        assertEquals(x.enabled, false)
        x.switchOn()
        assertEquals(x.enabled, true)
        x.switchOff()
        assertEquals(x.enabled, false)
        x.switch()
        assertEquals(x.enabled, true)
    }

    @Test
    fun smartPotTests() {
        val x = SmartPot("First", "myFav", Location.KITCHEN)
        assertFails{ x.boilWater() }
        x.waterVolume += 2.0
        x.boilWater()
        assertEquals(x.isWaterBoiled, true)
        assertFails{ x.waterVolume += 20 }
    }

    @Test
    fun smartThermometerTests() {
        val x = SmartThermometer("First", "myFav", Location.KITCHEN)
        x.temperature = 100
        assertEquals(x.temperature, 100)
        assertEquals(x.toString(), """
        Name = myFav
        Location = KITCHEN
        ...
        """.trimIndent())
    }

    @Test
    fun smartHomeTests() {
        val x = SmartHome("First", "myFavHome")
        val tv1 = SmartTv("firsttv", "myFavTv", Location.KITCHEN)
        val tv2 = SmartTv("secondtv", "myOtherTv", Location.ROOM)
        val th1 = SmartThermometer("firstth", "myFirstTh", Location.KITCHEN)
        val th2 = SmartThermometer("secondth", "mySecondTh", Location.ROOM)
        val th3 = SmartThermometer("thirdth", "myThirdTh", Location.ROOM)
        x.register(tv1)
        x.register(tv2)
        x.register(th1)
        x.register(th2)
        assertFails { x.register(th3) }
        assertArrayEquals(x.devices.toTypedArray(), arrayOf(tv1, tv2, th1, th2))
        assertArrayEquals(x.getDevicesByLocation(Location.KITCHEN).toTypedArray(), arrayOf(tv1, th1))
        assertEquals(x.getDeviceById("firsttv"), tv1)
        assertEquals(x.getDeviceById("aboba"), null)
    }

    @Test
    fun smartHomesControllerTests() {
        val x = SmartHomesController()
        val y = SmartHome("First", "myFavHOme")
        val z = SmartHome("Second", "mySecondHome")
        x.register(y)
        x.register(z)
        assertArrayEquals(x.getHomes().toTypedArray(), arrayOf(y, z))
        assertEquals(x.getHomeById("First"), y)
        x.unregister(y)
        assertEquals(x.getHomeById("First"), null)
        assertArrayEquals(x.getHomes().toTypedArray(), arrayOf(z))
    }
}