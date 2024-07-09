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
}
