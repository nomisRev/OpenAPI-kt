package io.github.nomisrev.openapi

import kotlin.test.Test
import kotlin.test.assertEquals

class ConstraintTest {
  @Test
  fun intRange() {
    assertEquals(
      Model.Primitive.int(
        constraint =
          Constraints.Number(
            minimum = 1.0,
            maximum = 10.0,
            exclusiveMinimum = false,
            exclusiveMaximum = false,
            multipleOf = null,
          )
      ),
      Schema(
          type = Schema.Type.Basic.Integer,
          minimum = 1.0,
          maximum = 10.0,
          exclusiveMinimum = false,
          exclusiveMaximum = false,
        )
        .toModel("IntRange"),
    )
  }

  @Test
  fun doubleRange() {
    assertEquals(
      Model.Primitive.double(
        constraint =
          Constraints.Number(
            minimum = 1.0,
            maximum = 10.0,
            exclusiveMinimum = false,
            exclusiveMaximum = false,
            multipleOf = null,
          )
      ),
      Schema(
          type = Schema.Type.Basic.Number,
          minimum = 1.0,
          maximum = 10.0,
          exclusiveMinimum = false,
          exclusiveMaximum = false,
        )
        .toModel("IntRange"),
    )
  }

  @Test
  fun text() {
    assertEquals(
      Model.Primitive.string(
        constraint = Constraints.Text(minLength = 1, maxLength = 10, pattern = null)
      ),
      Schema(type = Schema.Type.Basic.String, maxLength = 10, minLength = 1, pattern = null)
        .toModel("Text"),
    )
  }

  @Test
  fun list() {
    assertEquals(
      Model.Collection.list(
        inner = Model.Primitive.string(),
        constraint = Constraints.Collection(minItems = 1, maxItems = 10),
      ),
      Schema(
          type = Schema.Type.Basic.Array,
          items = ReferenceOr.value(Schema(type = Schema.Type.Basic.String)),
          maxItems = 10,
          minItems = 1,
        )
        .toModel("List"),
    )
  }

  @Test
  fun list_with_uniqueItems_constraint() {
    assertEquals(
      Model.Collection.list(
        inner = Model.Primitive.string(),
        constraint = Constraints.Collection(minItems = 1, maxItems = 10, uniqueItems = true),
      ),
      Schema(
          type = Schema.Type.Basic.Array,
          items = ReferenceOr.value(Schema(type = Schema.Type.Basic.String)),
          maxItems = 10,
          minItems = 1,
          uniqueItems = true,
        )
        .toModel("List"),
    )
  }

  @Test
  fun obj() {
    assertEquals(
      Model.obj(
        context = NamingContext.Named("Obj"),
        properties = listOf(Model.Object.property("name", Model.Primitive.string())),
        inline = listOf(Model.Primitive.string()),
      ),
      Schema(
          type = Schema.Type.Basic.Object,
          properties = mapOf("name" to ReferenceOr.value(Schema(type = Schema.Type.Basic.String))),
          minProperties = 1,
          maxProperties = 1,
        )
        .toModel("Obj"),
    )
  }
}
