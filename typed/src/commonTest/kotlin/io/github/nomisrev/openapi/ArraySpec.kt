package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.ReferenceOr.Companion.value
import io.github.nomisrev.openapi.Schema.Type
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Array specification conformance tests based on
 * https://swagger.io/docs/specification/v3_0/data-models/arrays/
 *
 * Goal: Verify that arrays keep `items` required semantics, and that `uniqueItems: true` is
 * enforced by validation while preserving a List type (do not switch to Set).
 */
class ArraySpec {
  @Test
  fun items_is_required_for_arrays() {
    val ex =
      assertFailsWith<IllegalArgumentException> {
        Schema(type = Type.Basic.Array).toModel("ArrayWithoutItems")
      }
    assertEquals("Array type requires items to be defined.", ex.message)
  }

  @Test
  fun uniqueItems_true_keeps_list_type_for_array_of_primitives() {
    val model =
      Schema(
          type = Type.Basic.Array,
          items = value(Schema(type = Type.Basic.Integer)),
          uniqueItems = true,
        )
        .toModel("Ints")

    // Desired behavior: Keep List + validate uniqueness at runtime/generation layer
    assertEquals(
      Model.Collection.list(
        Model.Primitive.int(),
        constraint =
          Constraints.Collection(minItems = 0, maxItems = Int.MAX_VALUE, uniqueItems = true),
      ),
      model,
    )
  }

  @Test
  fun uniqueItems_true_keeps_list_type_for_array_of_oneOf() {
    val model =
      Schema(
          type = Type.Basic.Array,
          items =
            value(
              Schema(
                oneOf =
                  listOf(
                    value(Schema(type = Type.Basic.String)),
                    value(Schema(type = Type.Basic.Integer)),
                  )
              )
            ),
          uniqueItems = true,
        )
        .toModel("Mixed")

    val inner =
      Model.union(
        context = NamingContext.Named("Mixed"),
        cases =
          listOf(
            Model.Union.Case(
              context = NamingContext.Named("Mixed"),
              model = Model.Primitive.string(),
            ),
            Model.Union.Case(context = NamingContext.Named("Mixed"), model = Model.Primitive.int()),
          ),
        inline = listOf(Model.Primitive.string(), Model.Primitive.int()),
      )

    // Desired behavior: Keep List + validate uniqueness at runtime/generation layer
    assertEquals(
      Model.Collection.list(
        inner,
        constraint =
          Constraints.Collection(minItems = 0, maxItems = Int.MAX_VALUE, uniqueItems = true),
      ),
      model,
    )
  }
}
