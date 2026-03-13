package io.github.nomisrev.openapi.parser.serializer

import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.Schema
import kotlinx.serialization.SerializationException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

class SchemaTypeSerializerTest {

    // ── JSON single string type ───────────────────────────────────────────────

    @Test
    fun `json single type string deserializes to Basic_String`() {
        val schema = Schema.fromJson("""{"type":"string"}""")
        assertEquals(Schema.Type.Basic.String, schema.type)
    }

    @Test
    fun `json single type integer deserializes to Basic_Integer`() {
        val schema = Schema.fromJson("""{"type":"integer"}""")
        assertEquals(Schema.Type.Basic.Integer, schema.type)
    }

    @Test
    fun `json single type number deserializes to Basic_Number`() {
        val schema = Schema.fromJson("""{"type":"number"}""")
        assertEquals(Schema.Type.Basic.Number, schema.type)
    }

    @Test
    fun `json single type boolean deserializes to Basic_Boolean`() {
        val schema = Schema.fromJson("""{"type":"boolean"}""")
        assertEquals(Schema.Type.Basic.Boolean, schema.type)
    }

    @Test
    fun `json single type object deserializes to Basic_Object`() {
        val schema = Schema.fromJson("""{"type":"object"}""")
        assertEquals(Schema.Type.Basic.Object, schema.type)
    }

    @Test
    fun `json single type array deserializes to Basic_Array`() {
        val schema = Schema.fromJson("""{"type":"array"}""")
        assertEquals(Schema.Type.Basic.Array, schema.type)
    }

    @Test
    fun `json single type null deserializes to Basic_Null`() {
        val schema = Schema.fromJson("""{"type":"null"}""")
        assertEquals(Schema.Type.Basic.Null, schema.type)
    }

    // ── JSON array types (OpenAPI 3.1.x) ─────────────────────────────────────

    @Test
    fun `json array type with string and null deserializes to Type_Array`() {
        val schema = Schema.fromJson("""{"type":["string","null"]}""")
        val arrayType = assertIs<Schema.Type.Array>(schema.type)
        assertEquals(listOf(Schema.Type.Basic.String, Schema.Type.Basic.Null), arrayType.types)
    }

    @Test
    fun `json array type with multiple types deserializes to Type_Array`() {
        val schema = Schema.fromJson("""{"type":["string","integer","boolean"]}""")
        val arrayType = assertIs<Schema.Type.Array>(schema.type)
        assertEquals(
            listOf(Schema.Type.Basic.String, Schema.Type.Basic.Integer, Schema.Type.Basic.Boolean),
            arrayType.types,
        )
    }

    @Test
    fun `json array type with single element deserializes to Type_Array`() {
        val schema = Schema.fromJson("""{"type":["string"]}""")
        val arrayType = assertIs<Schema.Type.Array>(schema.type)
        assertEquals(listOf(Schema.Type.Basic.String), arrayType.types)
    }

    @Test
    fun `json array type unknown values are filtered out`() {
        val schema = Schema.fromJson("""{"type":["string","unknown"]}""")
        val arrayType = assertIs<Schema.Type.Array>(schema.type)
        assertEquals(listOf(Schema.Type.Basic.String), arrayType.types)
    }

    // ── JSON invalid values ───────────────────────────────────────────────────

    @Test
    fun `json invalid string type throws SerializationException`() {
        assertFailsWith<SerializationException> {
            Schema.fromJson("""{"type":"foobar"}""")
        }
    }

    @Test
    fun `json numeric type value throws SerializationException`() {
        assertFailsWith<SerializationException> {
            Schema.fromJson("""{"type":42}""")
        }
    }

    // ── JSON serialization (roundtrip) ────────────────────────────────────────

    @Test
    fun `Basic type serializes back to string`() {
        val schema = Schema(type = Schema.Type.Basic.String)
        val json = OpenAPI.Json.encodeToString(Schema.serializer(), schema)
        assertTrue(json.contains("\"string\""), "Expected 'string' in: $json")
    }

    @Test
    fun `Array type serializes back to JSON array`() {
        val schema = Schema(type = Schema.Type.Array(listOf(Schema.Type.Basic.String, Schema.Type.Basic.Null)))
        val json = OpenAPI.Json.encodeToString(Schema.serializer(), schema)
        assertTrue(json.contains("["), "Expected array in: $json")
        assertTrue(json.contains("\"string\""), "Expected 'string' in: $json")
        assertTrue(json.contains("\"null\""), "Expected 'null' in: $json")
    }

    // ── YAML single string type ───────────────────────────────────────────────

    @Test
    fun `yaml single type string deserializes to Basic_String`() {
        val schema = Schema.fromYaml("type: string")
        assertEquals(Schema.Type.Basic.String, schema.type)
    }

    @Test
    fun `yaml single type integer deserializes to Basic_Integer`() {
        val schema = Schema.fromYaml("type: integer")
        assertEquals(Schema.Type.Basic.Integer, schema.type)
    }

    @Test
    fun `yaml single type boolean deserializes to Basic_Boolean`() {
        val schema = Schema.fromYaml("type: boolean")
        assertEquals(Schema.Type.Basic.Boolean, schema.type)
    }

    // ── YAML array types (OpenAPI 3.1.x) ─────────────────────────────────────

    @Test
    fun `yaml array type with string and null deserializes to Type_Array`() {
        val yaml = """
            type:
              - string
              - null
        """.trimIndent()
        val schema = Schema.fromYaml(yaml)
        val arrayType = assertIs<Schema.Type.Array>(schema.type)
        assertEquals(listOf(Schema.Type.Basic.String, Schema.Type.Basic.Null), arrayType.types)
    }

    @Test
    fun `yaml array type with multiple types deserializes to Type_Array`() {
        val yaml = """
            type:
              - string
              - integer
              - boolean
        """.trimIndent()
        val schema = Schema.fromYaml(yaml)
        val arrayType = assertIs<Schema.Type.Array>(schema.type)
        assertEquals(
            listOf(Schema.Type.Basic.String, Schema.Type.Basic.Integer, Schema.Type.Basic.Boolean),
            arrayType.types,
        )
    }

    // ── YAML invalid values ───────────────────────────────────────────────────

    @Test
    fun `yaml invalid string type throws SerializationException`() {
        assertFailsWith<SerializationException> {
            Schema.fromYaml("type: foobar")
        }
    }
}
