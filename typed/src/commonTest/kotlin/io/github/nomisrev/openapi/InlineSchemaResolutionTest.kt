package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.ReferenceOr.Companion.value
import io.github.nomisrev.openapi.Schema.Type
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class InlineSchemaResolutionTest {

  @Test
  fun `ComponentRegistry should create actual models during initialization`() {
    val userSchema = Schema(
      type = Type.Basic.Object,
      properties = mapOf(
        "id" to value(Schema(type = Type.Basic.String, description = value("User ID"))),
        "name" to value(Schema(type = Type.Basic.String, description = value("User name")))
      ),
      required = listOf("id", "name"),
      description = value("A user")
    )

    val spec = testAPI.copy(
      components = Components(schemas = mapOf("User" to value(userSchema)))
    )

    val context = TypedApiContext(spec)

    // Registry should contain the resolved models 
    val userModel = context.get("User")
    assertTrue(userModel is Model.Object, "User model should be an Object, got: ${userModel?.javaClass?.simpleName}")
    assertEquals(2, userModel.properties.size)
    assertEquals("id", userModel.properties[0].baseName)
    assertEquals("name", userModel.properties[1].baseName)
  }

  @Test
  fun `resolve with ForInline strategy should return actual models from registry`() {
    val userSchema = Schema(
      type = Type.Basic.Object,
      properties = mapOf(
        "name" to value(Schema(type = Type.Basic.String))
      )
    )

    val spec = testAPI.copy(
      components = Components(schemas = mapOf("User" to value(userSchema)))
    )

    val context = TypedApiContext(spec)

    with(context) {
      val reference = ReferenceOr.schema("User")
      val resolved = reference.resolve(SchemaResolutionStrategy.ForInline)

      when (resolved) {
        is Resolved.Ref -> assertEquals(resolved, Resolved.Ref("User", userSchema))
        is Resolved.Value<Schema> -> fail("Expected Resolved.Value but got Resolved.Ref")
      }
    }
  }

  @Test
  fun `resolve with ForComponents strategy should return actual models from registry`() {
    val userSchema = Schema(
      type = Type.Basic.Object,
      properties = mapOf(
        "name" to value(Schema(type = Type.Basic.String))
      )
    )

    val spec = testAPI.copy(
      components = Components(schemas = mapOf("User" to value(userSchema)))
    )

    val context = TypedApiContext(spec)

    with(context) {
      val reference = ReferenceOr.schema("User")
      val resolved = reference.resolve(SchemaResolutionStrategy.ForComponents)

      when (resolved) {
        is Resolved.Ref -> assertEquals(resolved, Resolved.Ref("User", userSchema))
        is Resolved.Value<Schema> -> fail("Expected Resolved.Value but got Resolved.Ref")
      }
    }
  }
}