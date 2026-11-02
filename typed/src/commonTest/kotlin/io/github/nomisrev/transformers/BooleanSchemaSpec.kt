package io.github.nomisrev.transformers

import de.infix.testBalloon.framework.core.testSuite
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.parser.ReferenceOr
import io.github.nomisrev.openapi.registry.Registry
import io.github.nomisrev.openapi.registry.toModel
import io.github.nomisrev.openapi.routes.SchemaContext
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * JSON Schema boolean schemas (OpenAPI 3.1): `true` accepts any instance (≡ `{}`), `false` accepts none.
 *
 * Most assertions compare the model of a schema using a boolean schema against the model of the
 * equivalent schema written without it, so they pin down semantics rather than incidental model shape.
 */
private fun components(schemas: String): OpenAPI =
    OpenAPI.fromJson(
        """
        {
          "openapi": "3.1.0",
          "info": { "title": "Boolean schemas", "version": "1" },
          "paths": {},
          "components": { "schemas": { $schemas } }
        }
        """.trimIndent()
    )

private suspend fun modelOf(schemas: String, name: String = "Value"): Model =
    Registry(components(schemas)).use { registry ->
        with(registry) {
            ReferenceOr.schema(name).toModel(NamingContext.reference(name, SchemaContext.Null), SchemaContext.Null)
        }
    }

/** Asserts [block] is rejected because of a boolean schema `false`, not for some unrelated reason. */
private suspend fun assertRejectsFalse(vararg mentions: String, block: suspend () -> Any?) {
    val error = assertFailsWith<IllegalArgumentException> { block() }
    (mentions.toList() + "boolean schema `false`").forEach { assertContains(error.message.orEmpty(), it) }
}

private const val NEVER = """"Never": false"""
private const val ANY = """"Any": true"""

val booleanSchemaSpec by testSuite {
    // ─── Object properties ─────────────────────────────────────────────────────

    test("codat: property named additionalProperties with schema false is not a field") {
        val actual = modelOf(
            """
            "Value": {
              "type": "object",
              "properties": { "additionalProperties": false }
            }
            """
        )
        assertEquals(
            Model.Object(
                context = NamingContext.reference("Value", SchemaContext.Null),
                description = null,
                title = null,
                properties = emptyMap(),
                additionalProperties = false,
                isNullable = false,
            ),
            actual
        )
    }

    test("property with schema false is dropped, sibling properties are kept") {
        assertEquals(
            modelOf(""""Value": { "type": "object", "properties": { "name": { "type": "string" } }, "required": ["name"] }"""),
            modelOf(
                """
                "Value": {
                  "type": "object",
                  "properties": { "name": { "type": "string" }, "forbidden": false },
                  "required": ["name"]
                }
                """
            )
        )
    }

    test("property referencing a false component is dropped") {
        assertEquals(
            modelOf(""""Value": { "type": "object", "properties": { "name": { "type": "string" } } }, $NEVER"""),
            modelOf(
                """
                "Value": {
                  "type": "object",
                  "properties": { "name": { "type": "string" }, "forbidden": { "${'$'}ref": "#/components/schemas/Never" } }
                },
                $NEVER
                """
            )
        )
    }

    test("required property with schema false makes the object unsatisfiable") {
        assertRejectsFalse("'forbidden'", "required") {
            modelOf(
                """
                "Value": {
                  "type": "object",
                  "properties": { "name": { "type": "string" }, "forbidden": false },
                  "required": ["forbidden"]
                }
                """
            )
        }
    }

    test("property with schema true is free-form JSON, like the empty schema") {
        assertEquals(
            modelOf(""""Value": { "type": "object", "properties": { "anything": {} } }"""),
            modelOf(""""Value": { "type": "object", "properties": { "anything": true } }""")
        )
    }

    // ─── oneOf / anyOf / allOf ─────────────────────────────────────────────────

    test("oneOf branch false can never match and is dropped") {
        assertEquals(
            modelOf(""""Value": { "oneOf": [{ "type": "string" }, { "type": "integer" }] }"""),
            modelOf(""""Value": { "oneOf": [{ "type": "string" }, false, { "type": "integer" }] }""")
        )
        assertEquals(
            modelOf(""""Value": { "oneOf": [{ "type": "string" }] }, $NEVER"""),
            modelOf(""""Value": { "oneOf": [{ "type": "string" }, { "${'$'}ref": "#/components/schemas/Never" }] }, $NEVER""")
        )
    }

    test("anyOf branch false can never match and is dropped") {
        assertEquals(
            modelOf(""""Value": { "anyOf": [{ "type": "string" }, { "type": "integer" }] }"""),
            modelOf(""""Value": { "anyOf": [{ "type": "string" }, { "type": "integer" }, false] }""")
        )
        assertEquals(
            modelOf(""""Value": { "anyOf": [{ "type": "string" }] }"""),
            modelOf(""""Value": { "anyOf": [false, { "type": "string" }] }""")
        )
    }

    test("oneOf / anyOf with only false branches is unsatisfiable") {
        assertRejectsFalse { modelOf(""""Value": { "oneOf": [false] }""") }
        assertRejectsFalse { modelOf(""""Value": { "anyOf": [false, false] }""") }
    }

    test("allOf containing false is unsatisfiable") {
        assertRejectsFalse {
            modelOf(""""Value": { "allOf": [{ "type": "object", "properties": { "a": { "type": "string" } } }, false] }""")
        }
    }

    // ─── Standalone positions ──────────────────────────────────────────────────

    test("component schema false has no representation") {
        assertRejectsFalse { modelOf(NEVER, name = "Never") }
    }

    test("component schema true is free-form JSON, like the empty schema") {
        assertEquals(modelOf(""""Any": {}""", name = "Any"), modelOf(ANY, name = "Any"))
    }

    test("items false (only the empty array is valid) has no representation") {
        assertRejectsFalse {
            modelOf(""""Value": { "type": "array", "items": false }""")
        }
    }

    test("items true is a list of free-form JSON, like the empty schema") {
        assertEquals(
            modelOf(""""Value": { "type": "array", "items": {} }"""),
            modelOf(""""Value": { "type": "array", "items": true }""")
        )
    }

    // ─── additionalProperties given as a $ref to a boolean schema ──────────────

    test("additionalProperties referencing a false schema is additionalProperties: false") {
        assertEquals(
            modelOf(""""Value": { "type": "object", "properties": { "a": { "type": "string" } }, "additionalProperties": false }"""),
            modelOf(
                """
                "Value": {
                  "type": "object",
                  "properties": { "a": { "type": "string" } },
                  "additionalProperties": { "${'$'}ref": "#/components/schemas/Never" }
                },
                $NEVER
                """
            )
        )
        assertEquals(
            modelOf(""""Value": { "type": "object", "additionalProperties": false }"""),
            modelOf(""""Value": { "type": "object", "additionalProperties": { "${'$'}ref": "#/components/schemas/Never" } }, $NEVER""")
        )
    }

    test("additionalProperties referencing a true schema is additionalProperties: true") {
        assertEquals(
            modelOf(""""Value": { "type": "object", "properties": { "a": { "type": "string" } }, "additionalProperties": true }"""),
            modelOf(
                """
                "Value": {
                  "type": "object",
                  "properties": { "a": { "type": "string" } },
                  "additionalProperties": { "${'$'}ref": "#/components/schemas/Any" }
                },
                $ANY
                """
            )
        )
        assertEquals(
            modelOf(""""Value": { "type": "object", "additionalProperties": true }"""),
            modelOf(""""Value": { "type": "object", "additionalProperties": { "${'$'}ref": "#/components/schemas/Any" } }, $ANY""")
        )
    }
}
