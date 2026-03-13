package io.github.nomisrev.openapi.parser.serializer

import io.github.nomisrev.openapi.parser.ExampleValue
import io.github.nomisrev.openapi.parser.OpenAPI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ExampleValueSerializerTest {

    // ── JSON primitive ────────────────────────────────────────────────────────

    @Test
    fun `json string primitive deserializes to Single`() {
        val result = OpenAPI.Json.decodeFromString(ExampleValue.serializer(), "\"hello\"")
        val single = assertIs<ExampleValue.Single>(result)
        assertEquals("hello", single.value)
    }

    @Test
    fun `json number primitive deserializes to Single`() {
        val result = OpenAPI.Json.decodeFromString(ExampleValue.serializer(), "42")
        val single = assertIs<ExampleValue.Single>(result)
        assertEquals("42", single.value)
    }

    @Test
    fun `json boolean primitive true deserializes to Single`() {
        val result = OpenAPI.Json.decodeFromString(ExampleValue.serializer(), "true")
        val single = assertIs<ExampleValue.Single>(result)
        assertEquals("true", single.value)
    }

    @Test
    fun `json boolean primitive false deserializes to Single`() {
        val result = OpenAPI.Json.decodeFromString(ExampleValue.serializer(), "false")
        val single = assertIs<ExampleValue.Single>(result)
        assertEquals("false", single.value)
    }

    @Test
    fun `json null primitive deserializes to Single with null string`() {
        val result = OpenAPI.Json.decodeFromString(ExampleValue.serializer(), "null")
        val single = assertIs<ExampleValue.Single>(result)
        assertEquals("null", single.value)
    }

    // ── JSON object ───────────────────────────────────────────────────────────

    @Test
    fun `json object deserializes to Single with stringified json`() {
        val result = OpenAPI.Json.decodeFromString(
            ExampleValue.serializer(),
            """{"name":"Alice","age":30}""",
        )
        val single = assertIs<ExampleValue.Single>(result)
        assertTrue(single.value.contains("name"), "Expected 'name' key in: ${single.value}")
        assertTrue(single.value.contains("Alice"), "Expected 'Alice' in: ${single.value}")
    }

    @Test
    fun `json nested object deserializes to Single`() {
        val result = OpenAPI.Json.decodeFromString(
            ExampleValue.serializer(),
            """{"a":{"b":"c"}}""",
        )
        assertIs<ExampleValue.Single>(result)
    }

    // ── JSON array of primitives ──────────────────────────────────────────────

    @Test
    fun `json array of all string primitives deserializes to Multiple`() {
        val result = OpenAPI.Json.decodeFromString(
            ExampleValue.serializer(),
            """["a","b","c"]""",
        )
        val multiple = assertIs<ExampleValue.Multiple>(result)
        assertEquals(listOf("a", "b", "c"), multiple.values)
    }

    @Test
    fun `json array of number primitives deserializes to Multiple`() {
        val result = OpenAPI.Json.decodeFromString(
            ExampleValue.serializer(),
            """[1,2,3]""",
        )
        val multiple = assertIs<ExampleValue.Multiple>(result)
        assertEquals(listOf("1", "2", "3"), multiple.values)
    }

    @Test
    fun `json array with non-primitive elements deserializes to Single`() {
        val result = OpenAPI.Json.decodeFromString(
            ExampleValue.serializer(),
            """["a",{"b":"c"}]""",
        )
        // Mixed array (primitive + object) → Single with JSON string
        assertIs<ExampleValue.Single>(result)
    }

    // ── JSON serialization (roundtrip) ────────────────────────────────────────

    @Test
    fun `Single serializes back to string`() {
        val value = ExampleValue.Single("hello")
        val json = OpenAPI.Json.encodeToString(ExampleValue.serializer(), value)
        assertEquals("\"hello\"", json.trim())
    }

    @Test
    fun `Multiple serializes back to JSON array`() {
        val value = ExampleValue.Multiple(listOf("x", "y"))
        val json = OpenAPI.Json.encodeToString(ExampleValue.serializer(), value)
        assertTrue(json.contains("["), "Expected array opening in: $json")
        assertTrue(json.contains("\"x\""), "Expected 'x' in: $json")
        assertTrue(json.contains("\"y\""), "Expected 'y' in: $json")
    }

    // ── YAML scalar ───────────────────────────────────────────────────────────

    @Test
    fun `yaml scalar string deserializes to Single`() {
        val result = OpenAPI.Yaml.decodeFromString(ExampleValue.serializer(), "hello")
        val single = assertIs<ExampleValue.Single>(result)
        assertEquals("hello", single.value)
    }

    @Test
    fun `yaml scalar number deserializes to Single`() {
        val result = OpenAPI.Yaml.decodeFromString(ExampleValue.serializer(), "42")
        val single = assertIs<ExampleValue.Single>(result)
        assertEquals("42", single.value)
    }

    @Test
    fun `yaml scalar boolean deserializes to Single`() {
        val result = OpenAPI.Yaml.decodeFromString(ExampleValue.serializer(), "true")
        val single = assertIs<ExampleValue.Single>(result)
        assertEquals("true", single.value)
    }

    // ── YAML object ───────────────────────────────────────────────────────────

    @Test
    fun `yaml map deserializes to Single with json-encoded string`() {
        val yaml = """
            name: Alice
            age: 30
        """.trimIndent()
        val result = OpenAPI.Yaml.decodeFromString(ExampleValue.serializer(), yaml)
        val single = assertIs<ExampleValue.Single>(result)
        assertTrue(single.value.contains("Alice"), "Expected 'Alice' in: ${single.value}")
    }

    // ── YAML array of scalars ─────────────────────────────────────────────────

    @Test
    fun `yaml list of all scalars deserializes to Multiple`() {
        val yaml = """
            - a
            - b
            - c
        """.trimIndent()
        val result = OpenAPI.Yaml.decodeFromString(ExampleValue.serializer(), yaml)
        val multiple = assertIs<ExampleValue.Multiple>(result)
        assertEquals(listOf("a", "b", "c"), multiple.values)
    }

    @Test
    fun `yaml list with non-scalar items deserializes to Single`() {
        val yaml = """
            - a
            - key: value
        """.trimIndent()
        val result = OpenAPI.Yaml.decodeFromString(ExampleValue.serializer(), yaml)
        // Mixed list → Single with JSON-encoded string
        assertIs<ExampleValue.Single>(result)
    }
}
