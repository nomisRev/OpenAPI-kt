package io.github.nomisrev.openapi.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

/**
 * A Path Item Object may be a `$ref` to another Path Item (`/owners: {$ref: '#/components/pathItems/Owners'}`).
 * The reference is kept when the document is read from JSON or YAML, and written back as `$ref`.
 */
class PathItemReferenceTest {
    private val json = $$"""
        {
          "openapi": "3.1.0",
          "info": { "title": "Api", "version": "0.0.1" },
          "paths": {
            "/owners": { "$ref": "#/components/pathItems/Owners", "summary": "Owners" }
          },
          "components": {
            "pathItems": {
              "Owners": { "get": { "responses": { "204": { "description": "No Content" } } } }
            }
          }
        }
    """.trimIndent()

    private val yaml = $$"""
        openapi: 3.1.0
        info:
          title: Api
          version: 0.0.1
        paths:
          /owners:
            $ref: '#/components/pathItems/Owners'
            summary: Owners
        components:
          pathItems:
            Owners:
              get:
                responses:
                  '204':
                    description: No Content
    """.trimIndent()

    @Test
    fun `a path item reference is read from JSON and YAML`() {
        for (spec in listOf(OpenAPI.fromJson(json), OpenAPI.fromYaml(yaml))) {
            val owners = spec.paths.paths.getValue("/owners")
            assertEquals("#/components/pathItems/Owners", owners.ref)
            assertEquals("Owners", owners.summary)
            assertNull(owners.get)
            val component = assertIs<ReferenceOr.Value<PathItem>>(spec.components.pathItems.getValue("Owners"))
            assertNull(component.value.ref)
        }
    }

    @Test
    fun `a path item reference is written back as a dollar ref`() {
        val spec = OpenAPI.fromJson(json)
        val expected = Json.parseToJsonElement(json).jsonObject.getValue("paths")
        assertEquals(expected, spec.toJsObject()["paths"])
        assertEquals(spec, OpenAPI.fromJson(spec.toJson()))
    }

    @Test
    fun `a path item reference inside a callback is read`() {
        val callback = OpenAPI.Json.decodeFromString(
            Callback.serializer(),
            $$"""{ "{$request.body#/url}": { "$ref": "#/components/pathItems/Owners" } }""",
        )
        assertEquals("#/components/pathItems/Owners", callback.value.getValue("{\$request.body#/url}").ref)
    }

    @Test
    fun `a webhook path item reference is read`() {
        val spec = OpenAPI.fromJson(
            $$"""
            {
              "openapi": "3.1.0",
              "info": { "title": "Api", "version": "0.0.1" },
              "webhooks": { "owner": { "$ref": "#/components/pathItems/Owners" } }
            }
            """.trimIndent()
        )
        val owner = assertIs<ReferenceOr.Reference>(spec.webhooks.getValue("owner"))
        assertEquals("#/components/pathItems/Owners", owner.ref)
    }
}
