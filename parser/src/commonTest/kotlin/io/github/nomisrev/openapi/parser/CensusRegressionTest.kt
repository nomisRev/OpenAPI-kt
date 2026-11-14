package io.github.nomisrev.openapi.parser

import com.goncalossilva.resources.Resource
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlin.test.assertEquals
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertIs
import kotlin.test.assertTrue

/** Census documents that once failed to parse, or lost data on the way through the model. */
class CensusRegressionTest {
    private fun parsed(name: String): OpenAPI {
        val json = OpenAPI.fromJson(Resource("specs/census/$name.json").readText())
        val yaml = OpenAPI.fromYaml(Resource("specs/census/$name.yaml").readText())
        assertEquals(json, yaml)
        assertEquals(json, OpenAPI.fromJson(json.toJson()))
        assertEquals(json, OpenAPI.fromYaml(json.toYaml()))
        return json
    }

    private fun enumOf(name: String): List<JsonElement> {
        val json = parsed(name)
        val schema = assertIs<ReferenceOr.Value<Schema>>(json.components.schemas.getValue("Value"))
        return assertNotNull(schema.value.enum)
    }

    @Test
    fun `array enum values are preserved`() {
        assertEquals(
            listOf(
                JsonPrimitive("all"),
                JsonPrimitive("none"),
                buildJsonArray { add(JsonPrimitive("project1")) },
            ),
            enumOf("apacta-array-enum"),
        )
        assertEquals(
            listOf<JsonElement>(buildJsonArray { add(JsonPrimitive(1)) }),
            enumOf("jobposting-array-enum"),
        )
    }

    @Test
    fun `object enum values are preserved`() {
        assertEquals(listOf<JsonElement>(buildJsonObject {}), enumOf("docker-object-enum"))
        val pandascore = enumOf("pandascore-object-enum")
        assertTrue(pandascore.isNotEmpty() && pandascore.all { it is JsonObject }, pandascore.toString())
        val enode = enumOf("enode-object-enum")
        assertTrue(enode.any { it is JsonObject } && JsonPrimitive("DEFAULT") in enode, enode.toString())
    }

    @Test
    fun `boolean property schema is parsed and round-trips`() {
        val json = OpenAPI.fromJson(Resource("specs/census/codat-boolean-property.json").readText())
        val yaml = OpenAPI.fromYaml(Resource("specs/census/codat-boolean-property.yaml").readText())
        assertEquals(json, yaml)

        val valueSchema = assertIs<ReferenceOr.Value<Schema>>(json.components.schemas.getValue("Value"))
        val propertySchema = assertIs<ReferenceOr.Value<Schema>>(
            valueSchema.value.properties?.getValue("additionalProperties")
        )
        assertEquals(false, propertySchema.value.booleanSchema)
        assertEquals(json, OpenAPI.fromJson(json.toJson()))
    }

    @Test
    fun `paths extension is preserved separately from path items`() {
        val json = OpenAPI.fromJson(Resource("specs/census/apicurio-paths-extension.json").readText())
        val yaml = OpenAPI.fromYaml(Resource("specs/census/apicurio-paths-extension.yaml").readText())
        assertEquals(json, yaml)
        assertTrue(json.paths.paths.isEmpty())
        assertEquals(mapOf("x-codegen-contextRoot" to JsonPrimitive("/apis/registry/v2")), json.paths.extensions)
        assertEquals(json, OpenAPI.fromJson(json.toJson()))
    }
}
