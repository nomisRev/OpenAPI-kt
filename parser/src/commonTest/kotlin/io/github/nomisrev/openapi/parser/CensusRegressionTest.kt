package io.github.nomisrev.openapi.parser

import com.goncalossilva.resources.Resource
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.assertEquals
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

/** Characterizes unresolved census failures; replace rejection with value assertions when fixed. */
class CensusRegressionTest {
    private fun rejects(name: String, jsonMessage: String, yamlMessage: String) {
        val json = Resource("specs/census/$name.json").readText()
        val yaml = Resource("specs/census/$name.yaml").readText()
        val jsonFailure = assertFailsWith<SerializationException> { OpenAPI.fromJson(json) }
        assertTrue(jsonFailure.message.orEmpty().contains(jsonMessage), jsonFailure.message)
        val yamlFailure = assertFailsWith<SerializationException> { OpenAPI.fromYaml(yaml) }
        assertTrue(yamlFailure.message.orEmpty().contains(yamlMessage), yamlFailure.message)
    }

    @Test
    fun `array enum values are currently rejected`() {
        rejects("apacta-array-enum", "JsonArray", "Expected a string, but got a list")
        rejects("jobposting-array-enum", "JsonArray", "Expected a string, but got a list")
    }

    @Test
    fun `object enum values are currently rejected`() {
        rejects("docker-object-enum", "JsonObject", "Expected a string, but got a map")
        rejects("pandascore-object-enum", "JsonObject", "Expected a string, but got a map")
        rejects("enode-object-enum", "JsonObject", "Expected a string, but got a map")
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
