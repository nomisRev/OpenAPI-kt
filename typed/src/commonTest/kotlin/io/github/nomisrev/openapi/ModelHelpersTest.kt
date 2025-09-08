package io.github.nomisrev.openapi

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ModelHelpersTest {
  @Test
  fun primitive_default_formats_values() {
    // Int
    assertEquals("42", Model.Primitive.Int(42, description = null, constraint = null).default())
    // Double
    assertEquals("3.14", Model.Primitive.Double(3.14, description = null, constraint = null).default())
    // Boolean
    assertEquals("true", Model.Primitive.Boolean(true, description = null).default())
    // String is quoted
    assertEquals("\"hello\"", Model.Primitive.String("hello", description = null, constraint = null).default())
    // Unit has no default
    assertNull(Model.Primitive.Unit(description = null).default())
  }

  @Test
  fun bodies_helpers_select_correct_variant() {
    val jsonBody = Route.Body.SetBody(Model.Primitive.String(null, null, null), description = null, extensions = emptyMap())
    val formBody = Route.Body.FormUrlEncoded(parameters = listOf(Route.Body.Multipart.FormData("name", Model.Primitive.String(null, null, null))), description = null, extensions = emptyMap())
    val multipartBody: Route.Body.Multipart = Route.Body.Multipart.Value(parameters = listOf(Route.Body.Multipart.FormData("file", Model.Primitive.String(null, null, null))), description = null, extensions = emptyMap())

    // Only SetBody present
    val setOnly = Route.Bodies(required = false, types = mapOf("application/json" to jsonBody), extensions = emptyMap())
    assertNotNull(setOnly.setBodyOrNull())
    assertNull(setOnly.formUrlEncodedOrNull())
    assertNull(setOnly.multipartOrNull())

    // Only FormUrlEncoded present
    val formOnly = Route.Bodies(required = false, types = mapOf("application/x-www-form-urlencoded" to formBody), extensions = emptyMap())
    assertNotNull(formOnly.formUrlEncodedOrNull())
    assertNull(formOnly.setBodyOrNull())
    assertNull(formOnly.multipartOrNull())

    // Only Multipart present
    val multiOnly = Route.Bodies(required = false, types = mapOf("multipart/form-data" to multipartBody), extensions = emptyMap())
    assertNotNull(multiOnly.multipartOrNull())
    assertNull(multiOnly.setBodyOrNull())
    assertNull(multiOnly.formUrlEncodedOrNull())
  }
}
