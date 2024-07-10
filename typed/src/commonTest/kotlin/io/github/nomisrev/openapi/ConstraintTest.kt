package io.github.nomisrev.openapi

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

class ConstraintTest {
  @Test
  fun intRange() {
    assertEquals(
      Model.Primitive.int(
        constraint =
          NumberConstraint(
            minimum = 1.0,
            maximum = 10.0,
            exclusiveMinimum = false,
            exclusiveMaximum = false,
            multipleOf = null
          )
      ),
      Schema(
          type = Schema.Type.Basic.Integer,
          minimum = 1.0,
          maximum = 10.0,
          exclusiveMinimum = false,
          exclusiveMaximum = false
        )
        .toModel("IntRange")
    )
  }

  @Test
  fun doubleRange() {
    assertEquals(
      Model.Primitive.double(
        constraint =
          NumberConstraint(
            minimum = 1.0,
            maximum = 10.0,
            exclusiveMinimum = false,
            exclusiveMaximum = false,
            multipleOf = null
          )
      ),
      Schema(
          type = Schema.Type.Basic.Number,
          minimum = 1.0,
          maximum = 10.0,
          exclusiveMinimum = false,
          exclusiveMaximum = false
        )
        .toModel("IntRange")
    )
  }

  @Test
  fun text() {
    assertEquals(
      Model.Primitive.string(
        constraint = TextConstraint(maxLength = 10, minLength = 1, pattern = null)
      ),
      Schema(type = Schema.Type.Basic.String, maxLength = 10, minLength = 1, pattern = null)
        .toModel("Text")
    )
  }

  @Test
  fun list() {
    assertEquals(
      Model.Collection.list(
        inner = Model.Primitive.string(),
        constraint = CollectionConstraint(minItems = 1, maxItems = 10)
      ),
      Schema(
          type = Schema.Type.Basic.Array,
          items = ReferenceOr.value(Schema(type = Schema.Type.Basic.String)),
          maxItems = 10,
          minItems = 1
        )
        .toModel("List")
    )
  }

  @Test
  fun set() {
    assertEquals(
      Model.Collection.set(
        inner = Model.Primitive.string(),
        constraint = CollectionConstraint(minItems = 1, maxItems = 10)
      ),
      Schema(
          type = Schema.Type.Basic.Array,
          items = ReferenceOr.value(Schema(type = Schema.Type.Basic.String)),
          maxItems = 10,
          minItems = 1,
          uniqueItems = true
        )
        .toModel("List")
    )
  }

  @Test
  fun obj() {
    assertEquals(
      Model.obj(
        context = NamingContext.Named("Obj"),
        properties = listOf(Model.Object.property("name", Model.Primitive.string())),
        inline = listOf(Model.Primitive.string()),
        constraint = ObjectConstraint(minProperties = 1, maxProperties = 1)
      ),
      Schema(
          type = Schema.Type.Basic.Object,
          properties = mapOf("name" to ReferenceOr.value(Schema(type = Schema.Type.Basic.String))),
          minProperties = 1,
          maxProperties = 1
        )
        .toModel("Obj")
    )
  }
}
