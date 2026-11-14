package io.github.nomisrev.openapi.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/** `enum` members, `default` and `example` keep the JSON value the document wrote, from JSON and YAML, and on output. */
class JsonValuePreservationTest {
    private fun json(text: String): JsonElement = Json.parseToJsonElement(text)

    private fun fromJson(schema: String): Schema = Schema.fromJson(schema)

    private fun fromYaml(schema: String): Schema = Schema.fromYaml(schema.trimIndent())

    private fun roundTrips(schema: Schema) {
        assertEquals(schema, Schema.fromJson(OpenAPI.Json.encodeToString(Schema.serializer(), schema)))
        assertEquals(schema, Schema.fromYaml(OpenAPI.Yaml.encodeToString(Schema.serializer(), schema)))
    }

    @Test
    fun `integer enum members stay numbers in json and yaml`() {
        val expected = listOf(JsonPrimitive(300), JsonPrimitive(600))
        assertEquals(expected, fromJson("""{"type":"integer","enum":[300,600]}""").enum)
        assertEquals(expected, fromYaml("type: integer\nenum: [300, 600]").enum)
    }

    @Test
    fun `boolean and number enum members keep their type`() {
        val schema = fromJson("""{"enum":[true,0.5,1.0]}""")
        assertEquals(listOf(JsonPrimitive(true), JsonPrimitive(0.5), JsonPrimitive(1.0)), schema.enum)
        assertEquals(schema, fromYaml("enum: [true, 0.5, 1.0]"))
    }

    @Test
    fun `json one and string one are distinct enum members`() {
        val schema = fromJson("""{"enum":[1,"1",true,"true"]}""")
        assertEquals(
            listOf(JsonPrimitive(1), JsonPrimitive("1"), JsonPrimitive(true), JsonPrimitive("true")),
            schema.enum,
        )
        assertEquals(schema, Schema.fromJson(OpenAPI.Json.encodeToString(Schema.serializer(), schema)))
        // toYaml writes the string members quoted; reading them back is limited by kaml, see the report.
        val yaml = OpenAPI.Yaml.encodeToString(Schema.serializer(), schema)
        assertEquals(true, "- 1\n" in yaml && "- \"1\"" in yaml && "- true\n" in yaml && "- \"true\"" in yaml, yaml)
    }

    @Test
    fun `json null enum member is null and the string null is a string`() {
        val schema = fromJson("""{"enum":["a",null,"null"]}""")
        assertEquals(listOf(JsonPrimitive("a"), JsonNull, JsonPrimitive("null")), schema.enum)
        assertEquals(schema, fromYaml("enum: [a, null, \"null\"]"))
        roundTrips(schema)
    }

    @Test
    fun `structured enum members parse in json and yaml`() {
        val expected = listOf(
            buildJsonObject {},
            buildJsonObject { put("id", 1); put("name", "LoL") },
            buildJsonArray { add(JsonPrimitive(1)); add(JsonPrimitive("x")) },
            JsonPrimitive("all"),
        )
        val schema = fromJson("""{"enum":[{},{"id":1,"name":"LoL"},[1,"x"],"all"]}""")
        assertEquals(expected, schema.enum)
        assertEquals(
            schema,
            fromYaml(
                """
                enum:
                  - {}
                  - id: 1
                    name: LoL
                  - [1, x]
                  - all
                """,
            ),
        )
        roundTrips(schema)
    }

    @Test
    fun `numeric default stays a number`() {
        assertEquals(JsonPrimitive(25), fromJson("""{"type":"number","default":25}""").default)
        assertEquals(JsonPrimitive(25), fromYaml("type: number\ndefault: 25").default)
        assertEquals(JsonPrimitive(1.5), fromJson("""{"type":"number","default":1.5}""").default)
        assertEquals(JsonPrimitive(true), fromJson("""{"type":"boolean","default":true}""").default)
    }

    @Test
    fun `string default of digits stays a string in json`() {
        assertEquals(JsonPrimitive("25"), fromJson("""{"type":"string","default":"25"}""").default)
    }

    @Test
    fun `explicit null default is distinct from no default and from the string null`() {
        assertNull(fromJson("""{"type":"string"}""").default)
        assertEquals(JsonNull, fromJson("""{"type":"string","nullable":true,"default":null}""").default)
        assertEquals(JsonNull, fromYaml("type: string\nnullable: true\ndefault: null").default)
        assertEquals(JsonPrimitive("null"), fromJson("""{"type":"string","default":"null"}""").default)
        assertEquals(JsonPrimitive("null"), fromYaml("type: string\ndefault: \"null\"").default)
        roundTrips(fromJson("""{"type":"string","nullable":true,"default":null}"""))
        roundTrips(fromJson("""{"type":"string","default":"null"}"""))
    }

    @Test
    fun `array and object defaults and examples stay structured`() {
        val array = fromJson("""{"type":"array","items":{"type":"integer"},"default":[1,2],"example":[3]}""")
        assertEquals(JsonArray(listOf(JsonPrimitive(1), JsonPrimitive(2))), array.default)
        assertEquals(JsonArray(listOf(JsonPrimitive(3))), array.example)
        assertEquals(array, fromYaml("type: array\nitems: {type: integer}\ndefault: [1, 2]\nexample: [3]"))
        roundTrips(array)

        val obj = fromJson("""{"type":"object","example":{"amount":18,"tags":["a"]},"default":{}}""")
        assertEquals(json("""{"amount":18,"tags":["a"]}"""), obj.example)
        assertEquals(JsonObject(emptyMap()), obj.default)
        roundTrips(obj)
    }

    @Test
    fun `parameter header and media type examples keep their json type`() {
        val api = OpenAPI.fromJson(
            """
            {"openapi":"3.0.3","info":{"title":"t","version":"1"},"paths":{"/p":{"get":{
              "parameters":[{"name":"n","in":"query","schema":{"type":"integer"},"example":5}],
              "responses":{"200":{"description":"ok",
                "headers":{"X":{"schema":{"type":"integer"},"example":7}},
                "content":{"application/json":{"example":{"a":[1,null]}}}}}}}}}
            """.trimIndent(),
        )
        val get = api.paths.paths.getValue("/p").get!!
        val parameter = (get.parameters.single() as ReferenceOr.Value).value
        assertEquals(JsonPrimitive(5), parameter.example)
        val response = (get.responses.responses.values.single() as ReferenceOr.Value).value
        assertEquals(JsonPrimitive(7), (response.headers.getValue("X") as ReferenceOr.Value).value.example)
        assertEquals(json("""{"a":[1,null]}"""), response.content.getValue("application/json").example)
        assertEquals(api, OpenAPI.fromJson(api.toJson()))
        assertEquals(api, OpenAPI.fromYaml(api.toYaml()))
    }

    @Test
    fun `toJson and toYaml write numbers as numbers`() {
        val api = OpenAPI.fromJson(
            """{"openapi":"3.1.0","info":{"title":"t","version":"1"},"components":{"schemas":{"V":{"type":"integer","enum":[300,600],"default":300}}}}""",
        )
        val written = json(api.toJson()).let { it as JsonObject }
        val schema = ((written.getValue("components") as JsonObject).getValue("schemas") as JsonObject).getValue("V") as JsonObject
        assertEquals(json("[300,600]"), schema["enum"])
        assertEquals(JsonPrimitive(300), schema["default"])
        val yaml = api.toYaml()
        assertEquals(true, "- 300" in yaml && "- 600" in yaml && "\"default\": 300" in yaml, yaml)
    }
}
