package io.github.nomisrev.openapi

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Reproducer for the current behavior where we "modify" the schema by flattening anyOf/oneOf with a
 * single element into the inner schema, instead of preserving the structural wrapper as a union
 * with one case.
 *
 * This is purely a characterization test to help us brainstorm a better approach without changing
 * behavior yet.
 */
class SchemaModificationReproducerTest {

  @Test
  fun anyOf_with_single_string_is_flattened_to_string() {
    val schema =
      Schema(
        description = ReferenceOr.Value("ID of the model to use."),
        anyOf = listOf(ReferenceOr.Value(Schema(type = Schema.Type.Basic.String))),
      )

    val api =
      OpenAPI(
        info = Info(title = "reproducer", version = "0.0.1"),
        components = Components(schemas = mapOf("Model" to ReferenceOr.Value(schema))),
      )

    val models = api.models()

    assertEquals(
      setOf(Model.Primitive.String(null, description = "ID of the model to use.", null)),
      models,
    )
  }

  @Test
  fun oneOf_with_single_string_is_flattened_to_string() {
    val schema =
      Schema(
        description = ReferenceOr.Value("Some description"),
        oneOf = listOf(ReferenceOr.Value(Schema(type = Schema.Type.Basic.String))),
      )

    val api =
      OpenAPI(
        info = Info(title = "reproducer", version = "0.0.1"),
        components = Components(schemas = mapOf("SingleString" to ReferenceOr.Value(schema))),
      )

    val models = api.models()

    assertEquals(
      setOf(Model.Primitive.String(null, description = "Some description", null)),
      models,
    )
  }
}
