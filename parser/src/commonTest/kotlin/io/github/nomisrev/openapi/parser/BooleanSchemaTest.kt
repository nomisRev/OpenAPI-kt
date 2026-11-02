package io.github.nomisrev.openapi.parser

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull

class BooleanSchemaTest {

    private val schemaSerializer = ReferenceOr.serializer(Schema.serializer())

    private fun json(schema: String): Schema = Schema.fromJson(schema)

    private fun yaml(schema: String): Schema = Schema.fromYaml(schema)

    private fun ReferenceOr<Schema>?.booleanSchema(): Boolean? =
        assertIs<ReferenceOr.Value<Schema>>(this).value.booleanSchema

    @Test
    fun `bare true and false decode as boolean schemas`() {
        assertEquals(Schema(booleanSchema = true), json("true"))
        assertEquals(Schema(booleanSchema = false), json("false"))
        assertEquals(Schema(booleanSchema = true), yaml("true"))
        assertEquals(Schema(booleanSchema = false), yaml("false"))
    }

    @Test
    fun `object schema has no boolean schema`() {
        assertNull(json("""{"type":"string"}""").booleanSchema)
        assertNull(yaml("type: string").booleanSchema)
    }

    @Test
    fun `boolean schema encodes back to bare boolean`() {
        assertEquals("false", OpenAPI.Json.encodeToString(Schema.serializer(), Schema(booleanSchema = false)))
        assertEquals(
            "true",
            OpenAPI.Json.encodeToString(schemaSerializer, ReferenceOr.value(Schema(booleanSchema = true))),
        )
    }

    @Test
    fun `items accepts boolean schema`() {
        assertEquals(false, json("""{"type":"array","items":false}""").items.booleanSchema())
        assertEquals(false, yaml("type: array\nitems: false").items.booleanSchema())
    }

    @Test
    fun `properties accept boolean schemas`() {
        val jsonSchema = json("""{"properties":{"x":true,"y":false}}""")
        val yamlSchema = yaml("properties:\n  x: true\n  y: false")
        assertEquals(jsonSchema, yamlSchema)
        assertEquals(true, jsonSchema.properties?.get("x").booleanSchema())
        assertEquals(false, jsonSchema.properties?.get("y").booleanSchema())
    }

    @Test
    fun `composition keywords and not accept boolean schemas`() {
        val schema =
            json("""{"allOf":[true],"oneOf":[false,{"type":"string"}],"anyOf":[true],"not":false}""")
        assertEquals(listOf(true), schema.allOf?.map { it.booleanSchema() })
        assertEquals(false, schema.oneOf?.first().booleanSchema())
        assertEquals(Schema.Type.Basic.String, assertIs<ReferenceOr.Value<Schema>>(schema.oneOf?.get(1)).value.type)
        assertEquals(listOf(true), schema.anyOf?.map { it.booleanSchema() })
        assertEquals(false, schema.not.booleanSchema())

        val yamlSchema = yaml("allOf: [true]\noneOf: [false, {type: string}]\nanyOf: [true]\nnot: false")
        assertEquals(schema, yamlSchema)
    }

    @Test
    fun `additionalProperties boolean still decodes as Allowed`() {
        val jsonAp = assertIs<AdditionalProperties.Allowed>(json("""{"additionalProperties":false}""").additionalProperties)
        assertFalse(jsonAp.value)
        val yamlAp = assertIs<AdditionalProperties.Allowed>(yaml("additionalProperties: true").additionalProperties)
        assertEquals(true, yamlAp.value)
    }

    @Test
    fun `nested boolean schemas round-trip through json`() {
        val schema = json("""{"type":"object","properties":{"x":true},"items":false,"not":true}""")
        val encoded = OpenAPI.Json.encodeToString(Schema.serializer(), schema)
        val obj = Json.parseToJsonElement(encoded).jsonObject
        assertEquals(JsonPrimitive(true), obj.getValue("properties").jsonObject.getValue("x"))
        assertEquals(JsonPrimitive(false), obj.getValue("items"))
        assertEquals(schema, json(encoded))
    }
}
