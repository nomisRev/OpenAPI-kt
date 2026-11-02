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

    /** Decodes [schema] as it would appear inside a full document, under `components.schemas`. */
    private fun jsonInDocument(schema: String): Schema {
        val document =
            """{"openapi":"3.1.0","info":{"title":"t","version":"1"},"paths":{},"components":{"schemas":{"S":$schema}}}"""
        return assertIs<ReferenceOr.Value<Schema>>(OpenAPI.fromJson(document).components.schemas["S"]).value
    }

    private fun assertFromJsonMatchesDocument(schema: String) {
        val standalone = runCatching { json(schema) }
        val inDocument = runCatching { jsonInDocument(schema) }
        assertEquals(
            inDocument.isSuccess,
            standalone.isSuccess,
            "Schema.fromJson and OpenAPI.fromJson disagree on whether <$schema> decodes: " +
                "standalone=$standalone, inDocument=$inDocument",
        )
        assertEquals(inDocument.getOrNull(), standalone.getOrNull())
    }

    @Test
    fun `fromJson decodes boolean schemas like a full document`() {
        assertFromJsonMatchesDocument("true")
        assertFromJsonMatchesDocument("  false\n")
        // Unquoted literal accepted by the lenient OpenAPI.Json, decoded as a boolean by Schema.Serializer.
        assertFromJsonMatchesDocument("TRUE")
    }

    @Test
    fun `fromJson decodes object schemas like a full document`() {
        assertFromJsonMatchesDocument("""{"type":"string"}""")
        assertFromJsonMatchesDocument("""{"discriminator":{"propertyName":"petType","mapping":{"dog":"#/d"}}}""")
    }

    @Test
    fun `fromJson rejects malformed json like a full document`() {
        // Missing comma between "propertyName" and "mapping".
        assertFromJsonMatchesDocument("""{"discriminator":{"propertyName":"petType" "mapping":{"dog":"#/d"}}}""")
    }

    @Test
    fun `fromJson decodes like fromYaml`() {
        assertEquals(yaml("  false\n"), json("  false\n"))
        assertEquals(yaml("""{"type": "string", "items": false}"""), json("""{"type": "string", "items": false}"""))
    }
}
