package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.Model.Object
import io.github.nomisrev.openapi.Model.Primitive
import io.github.nomisrev.openapi.ReferenceOr.Companion.value
import io.github.nomisrev.openapi.Schema.Type
import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.api.assertThrows

class ModelTest {
  @Test
  fun empty() {
    val actual = testAPI.models()
    assertEquals(emptySet(), actual)
  }

  @Test
  fun emptySchema() {
    assertThrows<NotImplementedError> {
      Schema().toModel("Empty")
    }
  }

  @Test
  fun `object`() {
    val actual =
      testAPI
        .copy(
          components =
          Components(schemas = mapOf("Person" to value(personSchema), "Id" to value(idSchema)))
        )
        .models()
    val expected =
      setOf(
        Model.obj(
          context = NamingContext.Named("Person"),
          properties =
          listOf(
            Object.property(
              "id",
              id,
              isRequired = true,
              isNullable = false,
              description = "An explicit ID type"
            ),
            Object.property(
              "name",
              // TODO default, and description doesn't belong to `Model`,
              //   but to Route.Param, or Property.
              //   Same probably applies to validation, refactor then.
              Primitive.string(default = "John Doe", description = "The name of the person"),
              isRequired = true,
              isNullable = false,
              description = "The name of the person"
            ),
            Object.property(
              baseName = "age",
              model = Primitive.int(),
              isNullable = true,
            )
          ),
          description = "A person",
          inline =
          listOf(
            Primitive.string(default = "John Doe", description = "The name of the person"),
            Primitive.int()
          )
        ),
        id
      )
    assertEquals(expected, actual)
  }

  @Test
  fun freeForm() {
    assertEquals(
      Model.FreeFormJson(description = null),
      Schema(
        type = Type.Basic.Object,
        additionalProperties = AdditionalProperties.Allowed(true)
      ).toModel("FreeForm")
    )
  }

  @Test
  fun freeFormNotAllowedIsIllegal() {
    assertThrows<IllegalStateException> {
      Schema(
        type = Type.Basic.Object,
        additionalProperties = AdditionalProperties.Allowed(false)
      ).toModel("FreeForm")
    }
  }

  @Test
  fun topLevelOneOfWithInlinePrimitives() {
    val actual = Schema(
      oneOf = listOf(
        value(Schema(type = Type.Basic.String)),
        value(Schema(type = Type.Basic.Integer))
      ),
      default = ExampleValue("example")
    ).toModel("OneOf")
    val expected =
      Model.Union(
        context = NamingContext.Named("OneOf"),
        description = null,
        default = "example",
        cases =
        listOf(
          // Order is important for deserialization,
          // order swapped compared to originally
          Model.Union.Case(context = NamingContext.Named("OneOf"), model = Primitive.int()),
          Model.Union.Case(context = NamingContext.Named("OneOf"), model = Primitive.string())
        ),
        inline = listOf(Primitive.string(), Primitive.int())
      )
    assertEquals(expected, actual)
  }

  @Test
  fun openEnum() {
    val actual = Schema(
      description = "OpenEnum Desc",
      anyOf =
      listOf(
        value(enumSchema.copy(description = "Inner Enum Desc")),
        ReferenceOr.Value(
          Schema(type = Type.Basic.String, description = "OpenCase Desc")
        )
      ),
      default = ExampleValue("Custom-open-enum-value")
    ).toModel("OpenEnum")
    val expected =
      Model.Enum.Open(
        context = NamingContext.Named("OpenEnum"),
        values = listOf("Auto", "Manual"),
        default = "Custom-open-enum-value",
        description = "OpenEnum Desc"
      )
    assertEquals(expected, actual)
  }

  @Test
  fun anyOf() {
    val actual = Schema(
      description = "AnyOf Desc",
      anyOf = listOf(
        value(enumSchema),
        ReferenceOr.Value(
          Schema(type = Type.Basic.Integer, description = "Int Case Desc")
        )
      )
    ).toModel("AnyOf")
    val context =
      NamingContext.Nested(
        NamingContext.Named("AutoOrManual"),
        NamingContext.Named("AnyOf"),
      )
    val expected =
      Model.Union(
        context = NamingContext.Named("AnyOf"),
        description = "AnyOf Desc",
        cases =
        listOf(
          Model.Union.Case(context = context, model = enum.copy(context = context)),
          Model.Union.Case(
            context = NamingContext.Named("AnyOf"),
            model = Primitive.int(description = "Int Case Desc")
          )
        ),
        default = "Auto",
        inline = listOf(enum.copy(context = context), Primitive.int(description = "Int Case Desc"))
      )
    assertEquals(expected, actual)
  }

  @Test
  fun typeArray() {
    val actual = Schema(
      type = Type.Array(types = listOf(Type.Basic.Integer, Type.Basic.String))
    ).toModel("TypeArray")
    val context = NamingContext.Named("TypeArray")
    val expected =
      Model.union(
        context = context,
        cases =
        listOf(
          Model.Union.Case(context = context, model = Primitive.int()),
          Model.Union.Case(context = context, model = Primitive.string())
        ),
        inline = listOf(Primitive.int(), Primitive.string())
      )
    assertEquals(expected, actual)
  }

  @Test
  fun typeArraySingletonIsFlattened() {
    assertEquals(
      Primitive.int(),
      Schema(type = Type.Array(types = listOf(Type.Basic.Integer))).toModel("Int")
    )
  }

  @Test
  fun stringBinaryFormat() {
    assertEquals(
      Model.OctetStream(description = null),
      Schema(type = Type.Basic.String, format = "binary").toModel("Binary")
    )
  }

  @Test
  fun defaultArrayIsList() {
    assertEquals(
      Model.Collection.list(Primitive.int()),
      Schema(
        type = Type.Basic.Array,
        items = value(Schema(type = Type.Basic.Integer))
      ).toModel("List")
    )
  }

  @Test
  fun noUniqueItemsIsList() {
    assertEquals(
      Model.Collection.list(inner = Primitive.int()),
      Schema(
        type = Type.Basic.Array,
        items = value(Schema(type = Type.Basic.Integer)),
        uniqueItems = false
      ).toModel("List")
    )
  }

  @Test
  fun uniqueItemsIsSet() {
    assertEquals(
      Model.Collection.set(inner = Primitive.int()),
      Schema(
        type = Type.Basic.Array,
        items = value(Schema(type = Type.Basic.Integer)),
        uniqueItems = true
      ).toModel("Set")
    )
  }
}
