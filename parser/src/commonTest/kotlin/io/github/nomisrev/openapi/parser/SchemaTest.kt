package io.github.nomisrev.openapi.parser

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SchemaTest {

    // ── Defaults / empty schema ───────────────────────────────────────────────

    @Test
    fun `empty json object deserializes to Schema with all null defaults`() {
        val schema = Schema.fromJson("{}")
        assertNull(schema.title)
        assertNull(schema.description)
        assertNull(schema.type)
        assertNull(schema.format)
        assertNull(schema.nullable)
        assertNull(schema.example)
        assertNull(schema.default)
        assertNull(schema.deprecated)
        assertTrue(schema.required.isEmpty())
        assertNull(schema.properties)
        assertNull(schema.items)
        assertNull(schema.allOf)
        assertNull(schema.oneOf)
        assertNull(schema.anyOf)
        assertNull(schema.not)
        assertNull(schema.additionalProperties)
        assertNull(schema.discriminator)
        assertNull(schema.enum)
        assertNull(schema.`const`)
        assertNull(schema.id)
        assertNull(schema.anchor)
        assertNull(schema.recursiveAnchor)
    }

    @Test
    fun `empty yaml mapping deserializes to Schema with all null defaults`() {
        val schema = Schema.fromYaml("{}")
        assertNull(schema.title)
        assertNull(schema.type)
        assertTrue(schema.required.isEmpty())
    }

    // ── Basic types ───────────────────────────────────────────────────────────

    @Test
    fun `json type string deserializes`() {
        val schema = Schema.fromJson("""{"type":"string"}""")
        assertEquals(Schema.Type.Basic.String, schema.type)
    }

    @Test
    fun `json type integer deserializes`() {
        val schema = Schema.fromJson("""{"type":"integer"}""")
        assertEquals(Schema.Type.Basic.Integer, schema.type)
    }

    @Test
    fun `json type number deserializes`() {
        val schema = Schema.fromJson("""{"type":"number"}""")
        assertEquals(Schema.Type.Basic.Number, schema.type)
    }

    @Test
    fun `json type boolean deserializes`() {
        val schema = Schema.fromJson("""{"type":"boolean"}""")
        assertEquals(Schema.Type.Basic.Boolean, schema.type)
    }

    @Test
    fun `json type object deserializes`() {
        val schema = Schema.fromJson("""{"type":"object"}""")
        assertEquals(Schema.Type.Basic.Object, schema.type)
    }

    @Test
    fun `json type array deserializes`() {
        val schema = Schema.fromJson("""{"type":"array"}""")
        assertEquals(Schema.Type.Basic.Array, schema.type)
    }

    @Test
    fun `json type null deserializes`() {
        val schema = Schema.fromJson("""{"type":"null"}""")
        assertEquals(Schema.Type.Basic.Null, schema.type)
    }

    // ── title, description, format ────────────────────────────────────────────

    @Test
    fun `json title and description deserialize`() {
        val schema = Schema.fromJson("""{"title":"My Schema","description":"A description","type":"string"}""")
        assertEquals("My Schema", schema.title)
        val desc = assertIs<ReferenceOr.Value<String>>(schema.description)
        assertEquals("A description", desc.value)
        assertEquals(Schema.Type.Basic.String, schema.type)
    }

    @Test
    fun `json format deserializes`() {
        val schema = Schema.fromJson("""{"type":"string","format":"uuid"}""")
        assertEquals("uuid", schema.format)
    }

    @Test
    fun `json format date-time deserializes`() {
        val schema = Schema.fromJson("""{"type":"string","format":"date-time"}""")
        assertEquals("date-time", schema.format)
    }

    // ── nullable (3.0.x) ─────────────────────────────────────────────────────

    @Test
    fun `json nullable true deserializes`() {
        val schema = Schema.fromJson("""{"type":"string","nullable":true}""")
        assertEquals(true, schema.nullable)
    }

    @Test
    fun `json nullable false deserializes`() {
        val schema = Schema.fromJson("""{"type":"string","nullable":false}""")
        assertEquals(false, schema.nullable)
    }

    // ── 3.1.x array type ─────────────────────────────────────────────────────

    @Test
    fun `json type array string and null deserializes to Type_Array`() {
        val schema = Schema.fromJson("""{"type":["string","null"]}""")
        val arrayType = assertIs<Schema.Type.Array>(schema.type)
        assertEquals(listOf(Schema.Type.Basic.String, Schema.Type.Basic.Null), arrayType.types)
    }

    @Test
    fun `yaml type array string and null deserializes to Type_Array`() {
        val yaml = """
            type:
              - string
              - null
        """.trimIndent()
        val schema = Schema.fromYaml(yaml)
        val arrayType = assertIs<Schema.Type.Array>(schema.type)
        assertEquals(listOf(Schema.Type.Basic.String, Schema.Type.Basic.Null), arrayType.types)
    }

    // ── Numeric constraints ───────────────────────────────────────────────────

    @Test
    fun `json minimum and maximum deserialize`() {
        val schema = Schema.fromJson("""{"type":"number","minimum":0.0,"maximum":100.0}""")
        assertEquals(0.0, schema.minimum)
        assertEquals(100.0, schema.maximum)
    }

    @Test
    fun `json exclusiveMinimum and exclusiveMaximum as booleans deserialize`() {
        val schema =
            Schema.fromJson("""{"type":"number","minimum":0.0,"exclusiveMinimum":true,"maximum":100.0,"exclusiveMaximum":false}""")
        assertEquals(Schema.ExclusiveLimit.BooleanValue(true), schema.exclusiveMinimum)
        assertEquals(Schema.ExclusiveLimit.BooleanValue(false), schema.exclusiveMaximum)
    }

    @Test
    fun `json exclusiveMinimum and exclusiveMaximum as numbers deserialize`() {
        val schema = Schema.fromJson("""{"type":"number","exclusiveMinimum":0.0,"exclusiveMaximum":100.0}""")
        assertEquals(Schema.ExclusiveLimit.NumberValue(0.0), schema.exclusiveMinimum)
        assertEquals(Schema.ExclusiveLimit.NumberValue(100.0), schema.exclusiveMaximum)
    }

    @Test
    fun `yaml exclusiveMinimum and exclusiveMaximum as booleans deserialize`() {
        val schema = Schema.fromYaml(
            """
            type: number
            minimum: 0.0
            exclusiveMinimum: true
            maximum: 100.0
            exclusiveMaximum: false
            """.trimIndent()
        )
        assertEquals(Schema.ExclusiveLimit.BooleanValue(true), schema.exclusiveMinimum)
        assertEquals(Schema.ExclusiveLimit.BooleanValue(false), schema.exclusiveMaximum)
    }

    @Test
    fun `yaml exclusiveMinimum and exclusiveMaximum as numbers deserialize`() {
        val schema = Schema.fromYaml(
            """
            type: number
            exclusiveMinimum: 0.0
            exclusiveMaximum: 100.0
            """.trimIndent()
        )
        assertEquals(Schema.ExclusiveLimit.NumberValue(0.0), schema.exclusiveMinimum)
        assertEquals(Schema.ExclusiveLimit.NumberValue(100.0), schema.exclusiveMaximum)
    }

    @Test
    fun `json exclusive bounds preserve boolean values when encoded`() {
        val schema =
            Schema.fromJson("""{"type":"number","minimum":0.0,"exclusiveMinimum":true,"maximum":100.0,"exclusiveMaximum":false}""")
        val json = Json.parseToJsonElement(schema.toString()).jsonObject
        assertEquals(true, (json["exclusiveMinimum"] as JsonPrimitive).booleanOrNull)
        assertEquals(false, (json["exclusiveMaximum"] as JsonPrimitive).booleanOrNull)
    }

    @Test
    fun `json exclusive bounds preserve number values when encoded`() {
        val schema = Schema.fromJson("""{"type":"number","exclusiveMinimum":0.0,"exclusiveMaximum":100.0}""")
        val json = Json.parseToJsonElement(schema.toString()).jsonObject
        assertEquals(0.0, (json["exclusiveMinimum"] as JsonPrimitive).doubleOrNull)
        assertEquals(100.0, (json["exclusiveMaximum"] as JsonPrimitive).doubleOrNull)
    }

    @Test
    fun `json multipleOf deserializes`() {
        val schema = Schema.fromJson("""{"type":"number","multipleOf":5.0}""")
        assertEquals(5.0, schema.multipleOf)
    }

    // ── String constraints ────────────────────────────────────────────────────

    @Test
    fun `json minLength and maxLength deserialize`() {
        val schema = Schema.fromJson("""{"type":"string","minLength":1,"maxLength":255}""")
        assertEquals(1, schema.minLength)
        assertEquals(255, schema.maxLength)
    }

    @Test
    fun `json pattern deserializes`() {
        val schema = Schema.fromJson("""{"type":"string","pattern":"^[a-z]+$"}""")
        assertEquals("^[a-z]+$", schema.pattern)
    }

    // ── Array constraints ─────────────────────────────────────────────────────

    @Test
    fun `json items deserializes as inline schema`() {
        val schema = Schema.fromJson("""{"type":"array","items":{"type":"string"}}""")
        val items = assertIs<ReferenceOr.Value<Schema>>(schema.items)
        assertEquals(Schema.Type.Basic.String, items.value.type)
    }

    @Test
    fun `json items deserializes as reference`() {
        val schema = Schema.fromJson("""{"type":"array","items":{"${"$"}ref":"#/components/schemas/Item"}}""")
        val itemsRef = assertIs<ReferenceOr.Reference>(schema.items)
        assertEquals("#/components/schemas/Item", itemsRef.ref)
    }

    @Test
    fun `json minItems and maxItems deserialize`() {
        val schema = Schema.fromJson("""{"type":"array","minItems":1,"maxItems":10}""")
        assertEquals(1, schema.minItems)
        assertEquals(10, schema.maxItems)
    }

    @Test
    fun `json uniqueItems deserializes`() {
        val schema = Schema.fromJson("""{"type":"array","uniqueItems":true}""")
        assertEquals(true, schema.uniqueItems)
    }

    // ── Object constraints ────────────────────────────────────────────────────

    @Test
    fun `json required list deserializes`() {
        val schema = Schema.fromJson("""{"type":"object","required":["id","name"]}""")
        assertEquals(listOf("id", "name"), schema.required)
    }

    @Test
    fun `json minProperties and maxProperties deserialize`() {
        val schema = Schema.fromJson("""{"type":"object","minProperties":1,"maxProperties":5}""")
        assertEquals(1, schema.minProperties)
        assertEquals(5, schema.maxProperties)
    }

    // ── Properties ────────────────────────────────────────────────────────────

    @Test
    fun `json properties with inline schemas deserializes`() {
        val schema =
            Schema.fromJson("""{"type":"object","properties":{"id":{"type":"integer"},"name":{"type":"string"}}}""")
        assertNotNull(schema.properties)
        val idProp = assertIs<ReferenceOr.Value<Schema>>(schema.properties["id"])
        assertEquals(Schema.Type.Basic.Integer, idProp.value.type)
        val nameProp = assertIs<ReferenceOr.Value<Schema>>(schema.properties["name"])
        assertEquals(Schema.Type.Basic.String, nameProp.value.type)
    }

    @Test
    fun `json properties with ref deserializes`() {
        val schema =
            Schema.fromJson("""{"type":"object","properties":{"pet":{"${"$"}ref":"#/components/schemas/Pet"}}}""")
        val petProp = assertIs<ReferenceOr.Reference>(schema.properties?.get("pet"))
        assertEquals("#/components/schemas/Pet", petProp.ref)
    }

    @Test
    fun `yaml properties deserialize`() {
        val yaml = """
            type: object
            properties:
              id:
                type: integer
              name:
                type: string
        """.trimIndent()
        val schema = Schema.fromYaml(yaml)
        assertNotNull(schema.properties)
        val idProp = assertIs<ReferenceOr.Value<Schema>>(schema.properties["id"])
        assertEquals(Schema.Type.Basic.Integer, idProp.value.type)
    }

    // ── additionalProperties ──────────────────────────────────────────────────

    @Test
    fun `json additionalProperties true deserializes to Allowed true`() {
        val schema = Schema.fromJson("""{"type":"object","additionalProperties":true}""")
        val ap = assertIs<AdditionalProperties.Allowed>(schema.additionalProperties)
        assertEquals(true, ap.value)
    }

    @Test
    fun `json additionalProperties false deserializes to Allowed false`() {
        val schema = Schema.fromJson("""{"type":"object","additionalProperties":false}""")
        val ap = assertIs<AdditionalProperties.Allowed>(schema.additionalProperties)
        assertEquals(false, ap.value)
    }

    @Test
    fun `json additionalProperties inline schema deserializes to PSchema`() {
        val schema = Schema.fromJson("""{"type":"object","additionalProperties":{"type":"string"}}""")
        val ap = assertIs<AdditionalProperties.PSchema>(schema.additionalProperties)
        val inner = assertIs<ReferenceOr.Value<Schema>>(ap.value)
        assertEquals(Schema.Type.Basic.String, inner.value.type)
    }

    @Test
    fun `json additionalProperties ref deserializes to PSchema`() {
        val schema =
            Schema.fromJson("""{"type":"object","additionalProperties":{"${"$"}ref":"#/components/schemas/Foo"}}""")
        val ap = assertIs<AdditionalProperties.PSchema>(schema.additionalProperties)
        val apRef = assertIs<ReferenceOr.Reference>(ap.value)
        assertEquals("#/components/schemas/Foo", apRef.ref)
    }

    // ── Composition: allOf / oneOf / anyOf / not ──────────────────────────────

    @Test
    fun `json allOf with inline schemas deserializes`() {
        val schema = Schema.fromJson("""{"allOf":[{"type":"string"},{"minLength":1}]}""")
        assertNotNull(schema.allOf)
        assertEquals(2, schema.allOf.size)
        val first = assertIs<ReferenceOr.Value<Schema>>(schema.allOf[0])
        assertEquals(Schema.Type.Basic.String, first.value.type)
        val second = assertIs<ReferenceOr.Value<Schema>>(schema.allOf[1])
        assertEquals(1, second.value.minLength)
    }

    @Test
    fun `json allOf with refs deserializes`() {
        val schema =
            Schema.fromJson("""{"allOf":[{"${"$"}ref":"#/components/schemas/A"},{"${"$"}ref":"#/components/schemas/B"}]}""")
        assertNotNull(schema.allOf)
        val refA = assertIs<ReferenceOr.Reference>(schema.allOf[0])
        assertEquals("#/components/schemas/A", refA.ref)
        val refB = assertIs<ReferenceOr.Reference>(schema.allOf[1])
        assertEquals("#/components/schemas/B", refB.ref)
    }

    @Test
    fun `json oneOf deserializes`() {
        val schema = Schema.fromJson("""{"oneOf":[{"type":"string"},{"${"$"}ref":"#/components/schemas/B"}]}""")
        assertNotNull(schema.oneOf)
        assertEquals(2, schema.oneOf.size)
        assertIs<ReferenceOr.Value<Schema>>(schema.oneOf[0])
        assertIs<ReferenceOr.Reference>(schema.oneOf[1])
    }

    @Test
    fun `json anyOf deserializes`() {
        val schema = Schema.fromJson("""{"anyOf":[{"type":"string"},{"type":"integer"}]}""")
        assertNotNull(schema.anyOf)
        assertEquals(2, schema.anyOf.size)
    }

    @Test
    fun `json not deserializes as inline schema`() {
        val schema = Schema.fromJson("""{"not":{"type":"string"}}""")
        val notVal = assertIs<ReferenceOr.Value<Schema>>(schema.not)
        assertEquals(Schema.Type.Basic.String, notVal.value.type)
    }

    @Test
    fun `json not deserializes as reference`() {
        val schema = Schema.fromJson("""{"not":{"${"$"}ref":"#/components/schemas/Forbidden"}}""")
        val notRef = assertIs<ReferenceOr.Reference>(schema.not)
        assertEquals("#/components/schemas/Forbidden", notRef.ref)
    }

    // ── discriminator ─────────────────────────────────────────────────────────

    @Test
    fun `json discriminator propertyName only deserializes`() {
        val schema = Schema.fromJson("""{"discriminator":{"propertyName":"type"}}""")
        assertNotNull(schema.discriminator)
        assertEquals("type", schema.discriminator.propertyName)
        assertNull(schema.discriminator.mapping)
    }

    @Test
    fun `json discriminator with mapping deserializes`() {
        val schema =
            Schema.fromJson("""{
                |   "discriminator":{
                |       "propertyName":"petType"
                |       "mapping":{
                |           "dog":"#/components/schemas/Dog",
                |           "cat":"#/components/schemas/Cat"
                |       }
                |   }
                |}""".trimMargin())
        assertNotNull(schema.discriminator)
        assertEquals("petType", schema.discriminator.propertyName)
        assertEquals(
            mapOf("dog" to "#/components/schemas/Dog", "cat" to "#/components/schemas/Cat"),
            schema.discriminator.mapping
        )
    }

    @Test
    fun `yaml discriminator deserializes`() {
        val yaml = """
            discriminator:
              propertyName: petType
              mapping:
                dog: "#/components/schemas/Dog"
                cat: "#/components/schemas/Cat"
        """.trimIndent()
        val schema = Schema.fromYaml(yaml)
        assertEquals("petType", schema.discriminator?.propertyName)
        assertEquals("#/components/schemas/Dog", schema.discriminator?.mapping?.get("dog"))
    }

    // ── enum ──────────────────────────────────────────────────────────────────

    @Test
    fun `json enum with string values deserializes`() {
        val schema = Schema.fromJson("""{"type":"string","enum":["active","inactive","pending"]}""")
        assertEquals(listOf("active", "inactive", "pending"), schema.enum)
    }

    @Test
    fun `json enum with null entry deserializes`() {
        val schema = Schema.fromJson("""{"enum":["active",null]}""")
        assertNotNull(schema.enum)
        assertEquals(listOf("active", null), schema.enum)
    }

    // ── const ─────────────────────────────────────────────────────────────────

    @Test
    fun `json const object deserializes`() {
        val schema = Schema.fromJson("""{"const":{"nested":[1,true,null,"x"]}}""")
        assertEquals(
            JsonObject(
                mapOf(
                    "nested" to
                        JsonArray(
                            listOf(
                                JsonPrimitive(1),
                                JsonPrimitive(true),
                                JsonNull,
                                JsonPrimitive("x"),
                            )
                        )
                )
            ),
            schema.`const`,
        )
    }

    @Test
    fun `yaml const object deserializes`() {
        val yaml = """
            const:
              nested:
                - 1
                - true
                - null
                - x
        """.trimIndent()
        val schema = Schema.fromYaml(yaml)
        assertEquals(
            JsonObject(
                mapOf(
                    "nested" to
                        JsonArray(
                            listOf(
                                JsonPrimitive(1),
                                JsonPrimitive(true),
                                JsonNull,
                                JsonPrimitive("x"),
                            )
                        )
                )
            ),
            schema.`const`,
        )
    }

    @Test
    fun `json const round-trips string and numeric primitives`() {
        val original = Schema(
            `const` = JsonObject(
                mapOf(
                    "stringNumber" to JsonPrimitive("1"),
                    "number" to JsonPrimitive(1),
                    "boolString" to JsonPrimitive("true"),
                    "bool" to JsonPrimitive(true),
                    "nested" to JsonArray(listOf(JsonPrimitive("x"), JsonPrimitive(2), JsonNull)),
                )
            )
        )
        val json = OpenAPI.Json.encodeToString(Schema.serializer(), original)
        val decoded = Schema.fromJson(json)
        assertEquals(original, decoded)
    }

    // ── default and example ───────────────────────────────────────────────────

    @Test
    fun `json default string value deserializes`() {
        val schema = Schema.fromJson("""{"type":"string","default":"hello"}""")
        val default = assertIs<ExampleValue.Single>(schema.default)
        assertEquals("hello", default.value)
    }

    @Test
    fun `json example string value deserializes`() {
        val schema = Schema.fromJson("""{"type":"string","example":"example-value"}""")
        val example = assertIs<ExampleValue.Single>(schema.example)
        assertEquals("example-value", example.value)
    }

    // ── readOnly / writeOnly / deprecated ─────────────────────────────────────

    @Test
    fun `json readOnly true deserializes`() {
        val schema = Schema.fromJson("""{"type":"string","readOnly":true}""")
        assertEquals(true, schema.readOnly)
    }

    @Test
    fun `json writeOnly true deserializes`() {
        val schema = Schema.fromJson("""{"type":"string","writeOnly":true}""")
        assertEquals(true, schema.writeOnly)
    }

    @Test
    fun `json deprecated true deserializes`() {
        val schema = Schema.fromJson("""{"type":"string","deprecated":true}""")
        assertEquals(true, schema.deprecated)
    }

    // ── 3.1.x id, anchor, recursiveAnchor ────────────────────────────────────

    @Test
    fun `json dollar-id deserializes`() {
        val schema = Schema.fromJson("""{"${"$"}id":"https://example.com/schemas/address"}""")
        assertEquals("https://example.com/schemas/address", schema.id)
    }

    @Test
    fun `json dollar-anchor deserializes`() {
        val schema = Schema.fromJson("""{"${"$"}anchor":"myAnchor"}""")
        assertEquals("myAnchor", schema.anchor)
    }

    @Test
    fun `json dollar-recursiveAnchor true deserializes`() {
        val schema = Schema.fromJson("""{"${"$"}recursiveAnchor":true}""")
        assertEquals(true, schema.recursiveAnchor)
    }

    @Test
    fun `yaml dollar-id deserializes`() {
        val yaml = """
            ${"$"}id: "https://example.com/schemas/address"
        """.trimIndent()
        val schema = Schema.fromYaml(yaml)
        assertEquals("https://example.com/schemas/address", schema.id)
    }

    // ── externalDocs ──────────────────────────────────────────────────────────

    @Test
    fun `json externalDocs deserializes`() {
        val schema =
            Schema.fromJson("""{"externalDocs":{"url":"https://docs.example.com","description":"More info"}}""")
        assertNotNull(schema.externalDocs)
        assertEquals("https://docs.example.com", schema.externalDocs.url)
        assertEquals("More info", schema.externalDocs.description)
    }

    // ── xml ───────────────────────────────────────────────────────────────────

    @Test
    fun `json xml deserializes`() {
        val schema =
            Schema.fromJson("""{
                |"type":"object",
                |"xml":{
                |       "name":"animal",
                |       "namespace":"http://example.com/schema/sample",
                |       "prefix":"sample",
                |       "wrapped":false
                |   }
                |}""".trimMargin())
        assertNotNull(schema.xml)
        assertEquals("animal", schema.xml.name)
        assertEquals("http://example.com/schema/sample", schema.xml.namespace)
        assertEquals("sample", schema.xml.prefix)
        assertEquals(false, schema.xml.wrapped)
    }

    // ── Companion convenience factories ───────────────────────────────────────

    @Test
    fun `Schema_string companion produces correct schema`() {
        assertEquals(Schema.Type.Basic.String, Schema.string.type)
    }

    @Test
    fun `Schema_uuid companion produces string type with uuid format`() {
        assertEquals(Schema.Type.Basic.String, Schema.uuid.type)
        assertEquals("uuid", Schema.uuid.format)
    }

    @Test
    fun `Schema_integer companion produces correct schema`() {
        assertEquals(Schema.Type.Basic.Integer, Schema.integer.type)
    }

    @Test
    fun `Schema_boolean companion produces correct schema`() {
        assertEquals(Schema.Type.Basic.Boolean, Schema.boolean.type)
    }

    @Test
    fun `Schema_number companion produces correct schema`() {
        assertEquals(Schema.Type.Basic.Number, Schema.number.type)
    }

    @Test
    fun `Schema_NULL companion produces null type`() {
        assertEquals(Schema.Type.Basic.Null, Schema.NULL.type)
    }

    @Test
    fun `Schema_list companion wraps schema in array type`() {
        val listSchema = Schema.list(Schema.string)
        assertEquals(Schema.Type.Basic.Array, listSchema.type)
        val items = assertIs<ReferenceOr.Value<Schema>>(listSchema.items)
        assertEquals(Schema.Type.Basic.String, items.value.type)
    }

    @Test
    fun `Schema_list companion accepts ReferenceOr`() {
        val ref = ReferenceOr.schema("Item")
        val listSchema = Schema.list(ref)
        assertEquals(Schema.Type.Basic.Array, listSchema.type)
        val items = assertIs<ReferenceOr.Reference>(listSchema.items)
        assertEquals("#/components/schemas/Item", items.ref)
    }

    // ── Round-trip (JSON serialize → deserialize) ─────────────────────────────

    @Test
    fun `round-trip empty schema`() {
        val original = Schema()
        val json = OpenAPI.Json.encodeToString(Schema.serializer(), original)
        val decoded = Schema.fromJson(json)
        assertEquals(original, decoded)
    }

    @Test
    fun `round-trip schema with all basic fields`() {
        val original = Schema(
            title = "User",
            type = Schema.Type.Basic.Object,
            required = listOf("id", "name"),
            properties = mapOf(
                "id" to ReferenceOr.Value(Schema.integer),
                "name" to ReferenceOr.Value(Schema.string),
            ),
            minProperties = 1,
            maxProperties = 10,
        )
        val json = OpenAPI.Json.encodeToString(Schema.serializer(), original)
        val decoded = Schema.fromJson(json)
        assertEquals(original, decoded)
    }

    @Test
    fun `round-trip schema with composition`() {
        val original = Schema(
            oneOf = listOf(
                ReferenceOr.Value(Schema.string),
                ReferenceOr.Reference("#/components/schemas/Other"),
            )
        )
        val json = OpenAPI.Json.encodeToString(Schema.serializer(), original)
        val decoded = Schema.fromJson(json)
        assertEquals(original, decoded)
    }

    @Test
    fun `round-trip schema with array type 3_1_x`() {
        val original = Schema(
            type = Schema.Type.Array(listOf(Schema.Type.Basic.String, Schema.Type.Basic.Null))
        )
        val json = OpenAPI.Json.encodeToString(Schema.serializer(), original)
        val decoded = Schema.fromJson(json)
        assertEquals(original, decoded)
    }

    @Test
    fun `round-trip schema with discriminator and mapping`() {
        val original = Schema(
            discriminator = Schema.Discriminator(
                propertyName = "type",
                mapping = mapOf("dog" to "#/components/schemas/Dog"),
            )
        )
        val json = OpenAPI.Json.encodeToString(Schema.serializer(), original)
        val decoded = Schema.fromJson(json)
        assertEquals(original, decoded)
    }

    // ── ignoreUnknownKeys ─────────────────────────────────────────────────────

    @Test
    fun `json unknown fields are silently ignored`() {
        val schema = Schema.fromJson("""{"type":"string","x-custom":"value","unknownField":42}""")
        assertEquals(Schema.Type.Basic.String, schema.type)
    }

    // ── Nested properties ─────────────────────────────────────────────────────

    @Test
    fun `json deeply nested properties deserialize`() {
        val json = """
            {
              "type": "object",
              "properties": {
                "address": {
                  "type": "object",
                  "properties": {
                    "street": {"type": "string"},
                    "zip": {"type": "string", "pattern": "^[0-9]{5}${'$'}"}
                  }
                }
              }
            }
        """.trimIndent()
        val schema = Schema.fromJson(json)
        val addressProp = assertIs<ReferenceOr.Value<Schema>>(schema.properties?.get("address"))
        val streetProp = assertIs<ReferenceOr.Value<Schema>>(addressProp.value.properties?.get("street"))
        assertEquals(Schema.Type.Basic.String, streetProp.value.type)
        val zipProp = assertIs<ReferenceOr.Value<Schema>>(addressProp.value.properties?.get("zip"))
        assertEquals("^[0-9]{5}\$", zipProp.value.pattern)
    }
}
