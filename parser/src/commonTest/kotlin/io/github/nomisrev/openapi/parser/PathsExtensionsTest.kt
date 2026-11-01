package io.github.nomisrev.openapi.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

class PathsExtensionsTest {
  private val json = """
    {
      "info": {"title": "Paths", "version": "1"},
      "paths": {
        "/pets": {"summary": "Pets"},
        "/x-example": {"summary": "A real path"},
        "x-string": "context",
        "x-number": 42,
        "x-boolean": true,
        "x-null": null,
        "x-array": [1, "two", false],
        "x-object": {"nested": [null, true]}
      }
    }
  """.trimIndent()

  private val yaml = """
    info:
      title: Paths
      version: "1"
    paths:
      /pets:
        summary: Pets
      /x-example:
        summary: A real path
      x-string: context
      x-number: 42
      x-boolean: true
      x-null: null
      x-array: [1, "two", false]
      x-object:
        nested: [null, true]
  """.trimIndent()

  @Test
  fun `JSON and YAML preserve paths alongside arbitrary extension values`() {
    val expectedPaths = Json.parseToJsonElement(json).jsonObject.getValue("paths").jsonObject
    for (spec in listOf(OpenAPI.fromJson(json), OpenAPI.fromYaml(yaml))) {
      assertEquals(setOf("/pets", "/x-example"), spec.paths.paths.keys)
      assertEquals("Pets", spec.paths.paths["/pets"]?.summary)
      assertEquals("A real path", spec.paths.paths["/x-example"]?.summary)
      assertEquals(expectedPaths.filterKeys { it.startsWith("x-") }, spec.paths.extensions)
      val encoded = spec.toJsObject()
      assertEquals(expectedPaths, encoded["paths"])
      assertFalse("pathsExtensions" in encoded)
      assertEquals(spec, OpenAPI.fromJson(spec.toJson()))
    }
  }

  @Test
  fun `Paths serializer handles extensions without an OpenAPI document`() {
    val expected = Json.parseToJsonElement(json).jsonObject.getValue("paths")
    val paths = Json.decodeFromJsonElement(Paths.serializer(), expected)
    assertEquals(OpenAPI.fromJson(json).paths, paths)
    assertEquals(expected, Json.encodeToJsonElement(Paths.serializer(), paths))
  }

  @Test
  fun `missing and empty paths have no extensions`() {
    val withoutPaths = """{"info":{"title":"Paths","version":"1"}}"""
    val withEmptyPaths = """{"info":{"title":"Paths","version":"1"},"paths":{}}"""
    for (spec in listOf(OpenAPI.fromJson(withoutPaths), OpenAPI.fromJson(withEmptyPaths))) {
      assertTrue(spec.paths.paths.isEmpty())
      assertTrue(spec.paths.extensions.isEmpty())
      assertFalse("pathsExtensions" in spec.toJsObject())
      assertEquals(spec, OpenAPI.fromJson(spec.toJson()))
    }
  }
}
