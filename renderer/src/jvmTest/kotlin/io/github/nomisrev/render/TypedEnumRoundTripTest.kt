package io.github.nomisrev.render

import io.github.nomisrev.render.test.`enum`.typed.wire.Baudrate
import io.github.nomisrev.render.test.`enum`.typed.wire.Meter
import io.github.nomisrev.render.test.`enum`.typed.in_.union.Speed
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * An enum on an `integer`, `integer/int64`, `number` or `boolean` schema is a JSON number or boolean, not the JSON
 * string kotlinx.serialization gives a plain `enum class` (OAS 3.1.1 4.8.24, JSON Schema Validation 6.1.2: an
 * instance validates only if it is equal to a member, and `300` is not `"300"`). Decoding is strict: a value that
 * is not a member, or has another JSON type, is a [SerializationException] naming the enum. Uses the generated
 * golden `kotlinTestData/enum/typed-wire`.
 */
class TypedEnumRoundTripTest {
    private val json = Json

    @Test
    fun integerEnumIsAJsonNumber() {
        assertEquals("""{"rate":300}""", json.encodeToString(Meter(rate = Baudrate.`300`)))
        assertEquals(Meter(rate = Baudrate.`600`), json.decodeFromString<Meter>("""{"rate":600}"""))
    }

    @Test
    fun everyKindOfTypedEnumRoundTrips() {
        val meter = Meter(
            rate = Baudrate.`1200`,
            epoch = Meter.Epoch.`5000000000`,
            scale = Meter.Scale.`05`,
            on = Meter.On.False,
        )
        val wire = """{"rate":1200,"epoch":5000000000,"scale":0.5,"on":false}"""

        assertEquals(wire, json.encodeToString(meter))
        assertEquals(meter, json.decodeFromString<Meter>(wire))
        assertEquals(Meter.On.True, json.decodeFromString<Meter>("""{"rate":300,"on":true}""").on)
        assertEquals(Meter.Scale.`15`, json.decodeFromString<Meter>("""{"rate":300,"scale":1.5}""").scale)
    }

    @Test
    fun int32EnumIsAJsonNumber() {
        val meter = Meter(rate = Baudrate.`300`, priority = Meter.Priority.`2`)
        assertEquals("""{"rate":300,"priority":2}""", json.encodeToString(meter))
        assertEquals(meter, json.decodeFromString<Meter>("""{"rate":300,"priority":2}"""))
        assertFailsWith<SerializationException> { json.decodeFromString<Meter>("""{"rate":300,"priority":"2"}""") }
        assertFailsWith<SerializationException> { json.decodeFromString<Meter>("""{"rate":300,"priority":3}""") }
    }

    /** An integer enum next to a string is not an open enum, which is a string; it is told apart by its JSON type. */
    @Test
    fun anIntegerEnumNextToAStringIsAnOrdinaryUnion() {
        assertEquals(Speed.CaseLong.`600`, json.decodeFromString<Speed>("600"))
        assertEquals(Speed.CaseString("fast"), json.decodeFromString<Speed>("\"fast\""))
        assertEquals("600", json.encodeToString<Speed>(Speed.CaseLong.`600`))
        assertEquals("\"fast\"", json.encodeToString<Speed>(Speed.CaseString("fast")))
    }

    @Test
    fun nullableAndListPropertiesUseTheNumberToo() {
        val meter = Meter(rate = Baudrate.`300`, fallback = Baudrate.`600`, rates = listOf(Baudrate.`300`, Baudrate.`1200`))
        val wire = """{"rate":300,"fallback":600,"rates":[300,1200]}"""

        assertEquals(wire, json.encodeToString(meter))
        assertEquals(meter, json.decodeFromString<Meter>(wire))
        assertNull(json.decodeFromString<Meter>("""{"rate":300,"fallback":null}""").fallback)
        assertNull(json.decodeFromString<Meter>("""{"rate":300}""").fallback)
    }

    @Test
    fun aNumberAsAStringIsRefused() {
        val error = assertFailsWith<SerializationException> { json.decodeFromString<Meter>("""{"rate":"300"}""") }
        assertTrue(error.message.orEmpty().contains("Baudrate"), error.message)
        assertFailsWith<SerializationException> { json.decodeFromString<Meter>("""{"rate":300,"on":"true"}""") }
        assertFailsWith<SerializationException> { json.decodeFromString<Meter>("""{"rate":300,"scale":"0.5"}""") }
        assertFailsWith<SerializationException> { json.decodeFromString<Meter>("""{"rate":300,"epoch":"1"}""") }
        assertFailsWith<SerializationException> { json.decodeFromString<Meter>("""{"rate":true}""") }
    }

    @Test
    fun aNonMemberIsRefused() {
        val error = assertFailsWith<SerializationException> { json.decodeFromString<Meter>("""{"rate":999}""") }
        assertTrue(error.message.orEmpty().contains("Baudrate"), error.message)
        assertTrue(error.message.orEmpty().contains("999"), error.message)
        assertFailsWith<SerializationException> { json.decodeFromString<Meter>("""{"rate":300.5}""") }
        assertFailsWith<SerializationException> { json.decodeFromString<Meter>("""{"rate":300,"scale":0.75}""") }
        assertFailsWith<SerializationException> { json.decodeFromString<Meter>("""{"rate":300,"epoch":2}""") }
    }
}
