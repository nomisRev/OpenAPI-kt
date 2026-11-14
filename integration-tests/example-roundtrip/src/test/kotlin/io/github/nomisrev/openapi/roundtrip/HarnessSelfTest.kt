package io.github.nomisrev.openapi.roundtrip

import io.github.nomisrev.openapi.parser.OpenAPI
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Proves the harness can see the bugs it exists for, independent of any real spec: `selftest.model` below is written
 * by hand the way the generator renders `{"type": "integer", "enum": [300, 600]}` today (a plain `@Serializable enum
 * class`, issues/enum-and-default-values-stored-as-strings.md), next to a class that round-trips.
 */
class HarnessSelfTest {
    private val spec = """
        {
          "openapi": "3.0.3", "info": {"title": "SelfTest", "version": "1"},
          "paths": {"/meters": {"get": {"operationId": "list", "responses": {"200": {"description": "ok",
            "content": {"application/json": {"schema": {"${'$'}ref": "#/components/schemas/Meter"},
              "examples": {"a": {"value": {"rate": 300, "name": "x"}}, "b": {"value": {"name": "x", "extra": 1}},
                           "c": {"value": {"name": "x"}}, "d": {"value": {"name": 5}}}}}}}}}},
          "components": {"schemas": {
            "Meter": {"type": "object", "required": ["name"], "properties": {
              "name": {"type": "string", "example": "meter"},
              "rate": {"type": "integer", "enum": [300, 600], "example": 600}}}}}
        }
    """.trimIndent()

    private fun run(): Map<String, Finding> {
        val raw = loadRawSpec(spec, yaml = false)
        val collected = collectExamples(raw)
        val models = GeneratedModels(OpenAPI.fromJson(spec), "selftest.model", "selftest.api", javaClass.classLoader)
        val roundTripper = RoundTripper(Json, models, raw)
        return collected.cases.map(roundTripper::run).associateBy { it.id }
    }

    @Test
    fun `an integer enum encoded as a string is reported`() {
        val rate300 = run().filterKeys { it.endsWith("/examples/a/value") }.values.single()
        // Decoding `300` into the string-serialized enum fails: this is the wire bug of the issue.
        assertEquals(Classification.DecodeFailed, rate300.classification, rate300.toString())
        assertTrue("JsonDecodingException" in rate300.signature, rate300.signature)
    }

    @Test
    fun `undeclared keys, null and plain classes are classified`() {
        val findings = run()
        fun at(suffix: String) = findings.filterKeys { it.endsWith(suffix) }.values.single()
        assertEquals(Classification.ExampleNotInSchema, at("/examples/b/value").classification, at("/examples/b/value").toString())
        assertEquals(Classification.Ok, at("/examples/c/value").classification, at("/examples/c/value").toString())
        // A number where the schema says string: the example is wrong, not the model.
        assertEquals(Classification.ExampleNotInSchema, at("/examples/d/value").classification, at("/examples/d/value").toString())
        // Property-level example on a plain string property.
        assertEquals(Classification.Ok, at("/properties/name/example").classification, at("/properties/name/example").toString())
        // Property-level example on the integer-enum property.
        assertEquals(Classification.DecodeFailed, at("/properties/rate/example").classification, at("/properties/rate/example").toString())
    }

    @Test
    fun `a number that comes back as a string is lossy, not normalised`() {
        val diffs = diff(Json.parseToJsonElement("""{"rate":300,"n":1}"""), Json.parseToJsonElement("""{"rate":"300","n":1.0}""")) { emptyList() }
        assertEquals(listOf("$.rate: 300 -> \"300\" (number->string)"), diffs.map { it.text })
    }
}
