package io.github.nomisrev.render

import io.github.nomisrev.render.test.`enum`.typed.wire.Baudrate
import io.github.nomisrev.render.test.`enum`.typed.wire.Meter
import io.github.nomisrev.render.test.`enum`.typed.in_.union.Speed
import kotlinx.serialization.Serializable
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

    @Serializable private data class Longs(val rate: Long, val epoch: Long? = null)
    @Serializable private data class Ints(val priority: Int)
    @Serializable private data class Doubles(val scale: Double)
    @Serializable private data class Booleans(val on: Boolean)

    private fun <T> outcome(block: () -> T): String =
        try { "ok(${block()})" } catch (e: SerializationException) { "SerializationException" }

    /**
     * A typed enum decodes exactly like the primitive it wraps, so how lenient a quoted number, a quoted boolean or a
     * number of the wrong shape is, is decided by the `Json` instance and not by the generated code.
     */
    @Test
    fun decodesLikeThePrimitiveItWraps() {
        fun parity(enumWire: String, primitiveWire: String, enum: (String) -> Any?, primitive: (String) -> Any?) {
            val e = outcome { enum(enumWire) }
            val p = outcome { primitive(primitiveWire) }
            println("OBSERVED enum=$enumWire -> $e ; primitive=$primitiveWire -> $p")
            assertEquals(p.startsWith("ok"), e.startsWith("ok"), "enum $e vs primitive $p")
        }
        val long = { s: String -> json.decodeFromString<Longs>(s).rate }
        val enumLong = { s: String -> json.decodeFromString<Meter>(s).rate.value }
        parity("""{"rate":"300"}""", """{"rate":"300"}""", enumLong, long)
        parity("""{"rate":300.5}""", """{"rate":300.5}""", enumLong, long)
        parity("""{"rate":300.0}""", """{"rate":300.0}""", enumLong, long)
        parity("""{"rate":true}""", """{"rate":true}""", enumLong, long)
        parity(
            """{"rate":300,"epoch":"1"}""", """{"rate":300,"epoch":"1"}""",
            { json.decodeFromString<Meter>(it).epoch?.value }, { json.decodeFromString<Longs>(it).epoch },
        )
        parity(
            """{"rate":300,"priority":"2"}""", """{"priority":"2"}""",
            { json.decodeFromString<Meter>(it).priority?.value }, { json.decodeFromString<Ints>(it).priority },
        )
        parity(
            """{"rate":300,"scale":"0.5"}""", """{"scale":"0.5"}""",
            { json.decodeFromString<Meter>(it).scale?.value }, { json.decodeFromString<Doubles>(it).scale },
        )
        parity(
            """{"rate":300,"on":"true"}""", """{"on":"true"}""",
            { json.decodeFromString<Meter>(it).on?.value }, { json.decodeFromString<Booleans>(it).on },
        )
        parity(
            """{"rate":300,"on":1}""", """{"on":1}""",
            { json.decodeFromString<Meter>(it).on?.value }, { json.decodeFromString<Booleans>(it).on },
        )
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
