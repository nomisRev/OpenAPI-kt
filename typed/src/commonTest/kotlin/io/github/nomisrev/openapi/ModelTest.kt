package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.Model.Object
import io.github.nomisrev.openapi.Model.Object.Property
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
      testAPI.copy(components = Components(schemas = mapOf("Empty" to value(Schema())))).models()
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
        Object(
          context = NamingContext.Named("Person"),
          properties =
            listOf(
              Property(
                "id",
                id,
                isRequired = true,
                isNullable = false,
                description = "An explicit ID type"
              ),
              Property(
                "name",
                // TODO default, and description doesn't belong to `Model`,
                //   but to Route.Param, or Property.
                //   Same probably applies to validation, refactor then.
                Primitive.String(default = "John Doe", description = "The name of the person"),
                isRequired = true,
                isNullable = false,
                description = "The name of the person"
              ),
              Property(
                "age",
                Primitive.Int(default = null, description = null),
                isRequired = false,
                isNullable = true,
                description = null
              )
            ),
          description = "A person",
          inline =
            listOf(
              Primitive.String(default = "John Doe", description = "The name of the person"),
              Primitive.Int(default = null, description = null)
            )
        ),
        id
      )
    assertEquals(expected, actual)
  }

  @Test
  fun freeForm() {
    val actual =
      testAPI
        .copy(
          components =
            Components(
              schemas =
                mapOf(
                  "FreeForm" to
                    value(
                      Schema(
                        type = Type.Basic.Object,
                        additionalProperties = AdditionalProperties.Allowed(true)
                      )
                    )
                )
            )
        )
        .models()
    val expected = Model.FreeFormJson(description = null)
    assertEquals(setOf(expected), actual)
  }

  @Test
  fun freeFormNotAllowedIsIllegal() {
    assertThrows<IllegalStateException> {
      testAPI
        .copy(
          components =
            Components(
              schemas =
                mapOf(
                  "FreeForm" to
                    value(
                      Schema(
                        type = Type.Basic.Object,
                        additionalProperties = AdditionalProperties.Allowed(false)
                      )
                    )
                )
            )
        )
        .models()
        .let(::println)
    }
  }

  @Test
  fun topLevelOneOfWithInlinePrimitives() {
    val actual =
      testAPI
        .copy(
          components =
            Components(
              schemas =
                mapOf(
                  "OneOf" to
                    value(
                      Schema(
                        oneOf =
                          listOf(
                            value(Schema(type = Type.Basic.String)),
                            value(Schema(type = Type.Basic.Integer))
                          ),
                        default = ExampleValue("example")
                      )
                    )
                )
            )
        )
        .models()
    val expected =
      Model.Union(
        context = NamingContext.Named("OneOf"),
        description = null,
        default = "example",
        cases =
          listOf(
            // Order is important for deserialization,
            // order swapped compared to originally
            Model.Union.Case(
              context = NamingContext.Named("OneOf"),
              model = Primitive.Int(default = null, description = null)
            ),
            Model.Union.Case(
              context = NamingContext.Named("OneOf"),
              model = Primitive.String(default = null, description = null)
            )
          ),
        inline =
          listOf(
            Primitive.String(default = null, description = null),
            Primitive.Int(default = null, description = null)
          )
      )
    assertEquals(setOf(expected), actual)
  }

  @Test
  fun openEnum() {
    val actual =
      testAPI
        .copy(
          components =
            Components(
              schemas =
                mapOf(
                  "OpenEnum" to
                    value(
                      Schema(
                        description = "OpenEnum Desc",
                        anyOf =
                          listOf(
                            value(enumSchema.copy(description = "Inner Enum Desc")),
                            ReferenceOr.Value(
                              Schema(type = Type.Basic.String, description = "OpenCase Desc")
                            )
                          ),
                        default = ExampleValue("Custom-open-enum-value")
                      )
                    )
                )
            )
        )
        .models()
    val expected =
      Model.Enum.Open(
        context = NamingContext.Named("OpenEnum"),
        values = listOf("Auto", "Manual"),
        default = "Custom-open-enum-value",
        description = "OpenEnum Desc"
      )
    assertEquals(setOf(expected), actual)
  }

  @Test
  fun anyOf() {
    val actual =
      testAPI
        .copy(
          components =
            Components(
              schemas =
                mapOf(
                  "AnyOf" to
                    value(
                      Schema(
                        description = "AnyOf Desc",
                        anyOf =
                          listOf(
                            value(enumSchema),
                            ReferenceOr.Value(
                              Schema(type = Type.Basic.Integer, description = "Int Case Desc")
                            )
                          )
                      )
                    )
                )
            )
        )
        .models()
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
              model = Primitive.Int(default = null, description = "Int Case Desc")
            )
          ),
        default = "Auto",
        inline =
          listOf(
            enum.copy(context = context),
            Primitive.Int(default = null, description = "Int Case Desc")
          )
      )
    assertEquals(setOf(expected), actual)
  }

  @Test
  fun typeArray() {
    val actual =
      testAPI
        .copy(
          components =
            Components(
              schemas =
                mapOf(
                  "TypeArray" to
                    value(
                      Schema(
                        type = Type.Array(types = listOf(Type.Basic.Integer, Type.Basic.String))
                      )
                    )
                )
            )
        )
        .models()
    val expected =
      Model.Union(
        context = NamingContext.Named("TypeArray"),
        description = null,
        cases =
          listOf(
            Model.Union.Case(
              context = NamingContext.Named("TypeArray"),
              model = Primitive.Int(default = null, description = null)
            ),
            Model.Union.Case(
              context = NamingContext.Named("TypeArray"),
              model = Primitive.String(default = null, description = null)
            )
          ),
        default = null,
        inline =
          listOf(
            Primitive.Int(default = null, description = null),
            Primitive.String(default = null, description = null)
          )
      )
    assertEquals(setOf(expected), actual)
  }

  @Test
  fun typeArraySingleton() {
    val actual =
      testAPI
        .copy(
          components =
            Components(
              schemas =
                mapOf(
                  "TypeArray" to
                    value(Schema(type = Type.Array(types = listOf(Type.Basic.Integer))))
                )
            )
        )
        .models()
    val expected = Primitive.Int(default = null, description = null)
    assertEquals(setOf(expected), actual)
  }

  @Test
  fun binary() {
    val actual =
      testAPI
        .copy(
          components =
            Components(
              schemas =
                mapOf(
                  "Primitive.Binary" to value(Schema(type = Type.Basic.String, format = "binary"))
                )
            )
        )
        .models()
    val expected = Model.OctetStream(description = null)
    assertEquals(setOf(expected), actual)
  }

  @Test
  fun defaultArrayIsList() {
    val actual =
      testAPI
        .copy(
          components =
            Components(
              schemas =
                mapOf(
                  "Primitive.Array" to
                    value(
                      Schema(
                        type = Type.Basic.Array,
                        items = value(Schema(type = Type.Basic.Integer))
                      )
                    )
                )
            )
        )
        .models()
    val expected =
      Model.Collection.List(
        inner = Primitive.Int(default = null, description = null),
        description = null,
        default = null
      )
    assertEquals(setOf(expected), actual)
  }

  @Test
  fun noUniqueItemsIsList() {
    val actual =
      testAPI
        .copy(
          components =
            Components(
              schemas =
                mapOf(
                  "Primitive.Array" to
                    value(
                      Schema(
                        type = Type.Basic.Array,
                        items = value(Schema(type = Type.Basic.Integer)),
                        uniqueItems = false
                      )
                    )
                )
            )
        )
        .models()
    val expected =
      Model.Collection.List(
        inner = Primitive.Int(default = null, description = null),
        description = null,
        default = null
      )
    assertEquals(setOf(expected), actual)
  }

  @Test
  fun uniqueItemsIsSet() {
    val actual =
      testAPI
        .copy(
          components =
            Components(
              schemas =
                mapOf(
                  "Primitive.Set" to
                    value(
                      Schema(
                        type = Type.Basic.Array,
                        items = value(Schema(type = Type.Basic.Integer)),
                        uniqueItems = true
                      )
                    )
                )
            )
        )
        .models()
    val expected =
      Model.Collection.Set(
        inner = Primitive.Int(default = null, description = null),
        description = null,
        default = null
      )
    assertEquals(setOf(expected), actual)
  }
}
