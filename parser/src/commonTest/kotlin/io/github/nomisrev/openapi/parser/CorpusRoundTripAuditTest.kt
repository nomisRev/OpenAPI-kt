package io.github.nomisrev.openapi.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

/** Characterizations of unresolved data loss, not assertions of correct OpenAPI behavior. */
class CorpusRoundTripAuditTest {
    private fun roundTripSchema(schema: String): JsonObject {
        val document = """{"openapi":"3.1.0","info":{"title":"Audit","version":"1"},"components":{"schemas":{"Name":{"type":"object"},"Value":$schema}}}"""
        return Json.parseToJsonElement(OpenAPI.fromJson(document).toJson()).jsonObject
            .getValue("components").jsonObject.getValue("schemas").jsonObject.getValue("Value").jsonObject
    }

    @Test
    fun `numeric enum members currently become strings`() {
        // mbus.local: /components/schemas/baudrate
        val actual = roundTripSchema("""{"type":"integer","enum":[300,600]}""")
        assertEquals(Json.parseToJsonElement("""["300","600"]"""), actual["enum"])
    }

    @Test
    fun `numeric defaults currently become strings`() {
        // icons8.com: amount parameter schema
        val actual = roundTripSchema("""{"type":"number","default":25}""")
        assertEquals(Json.parseToJsonElement(""""25""""), actual["default"])
    }

    @Test
    fun `object examples currently become JSON encoded strings`() {
        // change.local: carbon_calculate response media example
        val actual = roundTripSchema("""{"type":"object","example":{"amount":18}}""")
        assertEquals(Json.parseToJsonElement(""""{\"amount\":18}""""), actual["example"])
    }

    @Test
    fun `OpenAPI 3_1 schema reference siblings are currently discarded`() {
        // Codat Commerce: TaxComponent.allOf[2] (reference target renamed in this reduction)
        val actual = roundTripSchema("""{"${'$'}ref":"#/components/schemas/Name","required":["name"]}""")
        assertEquals(Json.parseToJsonElement("""{"${'$'}ref":"#/components/schemas/Name"}"""), actual)
    }

    @Test
    fun `root extensions are currently discarded`() {
        // AWSMigrationHub: root x-hasEquivalentPaths
        val api = OpenAPI.fromJson("""{"openapi":"3.0.0","info":{"title":"Audit","version":"1"},"paths":{},"x-hasEquivalentPaths":true}""")
        assertEquals(emptyMap(), api.extensions)
        assertFalse("x-hasEquivalentPaths" in Json.parseToJsonElement(api.toJson()).jsonObject)
    }
}
