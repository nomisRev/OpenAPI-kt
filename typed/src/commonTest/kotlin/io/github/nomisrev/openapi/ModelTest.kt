package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.Model.Object
import io.github.nomisrev.openapi.Model.Object.Property
import io.github.nomisrev.openapi.Model.Primitive
import io.github.nomisrev.openapi.Schema.Type
import kotlin.test.Test
import kotlin.test.assertEquals

class ModelTest {
  private val idSchema =
    Schema(
      type = Type.Basic.Object,
      description = "An explicit ID type",
      properties = mapOf("value" to ReferenceOr.value(Schema(type = Type.Basic.String)))
    )

  private val personSchema =
    Schema(
      type = Type.Basic.Object,
      properties =
      mapOf(
        "id" to ReferenceOr.schema("Id"),
        "name" to
          ReferenceOr.value(
            Schema(
              type = Type.Basic.String,
              nullable = false,
              description = "The name of the person",
              default = ExampleValue("John Doe")
            )
          ),
        "age" to ReferenceOr.value(Schema(type = Type.Basic.Integer)),
      ),
      description = "A person",
      required = listOf("id", "name")
    )

  private val id =
    Object(
      context = NamingContext.Named("Id"),
      description = "An explicit ID type",
      properties =
      listOf(
        Property(
          "value",
          Primitive.String(default = null, description = null),
          isRequired = false,
          isNullable = true,
          description = null
        )
      ),
      inline = listOf(Primitive.String(default = null, description = null))
    )

  private val testAPI = OpenAPI(info = Info("Test API", version = "1.0.0"))

  @Test
  fun `object`() {
    val actual =
      testAPI
        .copy(
          components =
          Components(
            schemas =
            mapOf(
              "Person" to ReferenceOr.value(personSchema),
              "Id" to ReferenceOr.value(idSchema)
            )
          )
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
  fun topLevelOneOfWithInlinePrimitives() {
    val actual =
      testAPI.copy(
        components = Components(
          schemas = mapOf(
            "OneOf" to ReferenceOr.value(
              Schema(
                oneOf = listOf(
                  ReferenceOr.value(Schema(type = Type.Basic.String)),
                  ReferenceOr.value(Schema(type = Type.Basic.Integer))
                ),
                default = ExampleValue("example")
              )
            )
          )
        )
      ).models()
    val expected = Model.Union(
      context = NamingContext.Named("OneOf"),
      description = null,
      default = "example",
      cases = listOf(
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
      inline = listOf(
        Primitive.String(default = null, description = null),
        Primitive.Int(default = null, description = null)
      )
    )
    assertEquals(setOf(expected), actual)
  }

  @Test
  fun topLevelOneOfWithInlineEnumAndDefault() {
    val actual =
      testAPI.copy(
        components = Components(
          schemas = mapOf(
            "OneOf" to ReferenceOr.value(
              Schema(
                oneOf = listOf(
                  ReferenceOr.value(Schema(type = Type.Basic.String)),
                  ReferenceOr.value(
                    Schema(
                      type = Type.Basic.String,
                      enum = listOf("Auto", "Manual"),
                      default = ExampleValue("Auto")
                    )
                  )
                )
              )
            )
          )
        )
      ).models()
    val enum = Model.Enum.Closed(
      context = NamingContext.Nested(
        NamingContext.Named("AutoOrManual"),
        NamingContext.Named("OneOf")
      ),
      inner = Primitive.String(default = "Auto", description = null),
      values = listOf("Auto", "Manual"),
      default = "Auto",
      description = null
    )
    val expected = Model.Union(
      context = NamingContext.Named("OneOf"),
      description = null,
      default = "Auto",
      cases = listOf(
        Model.Union.Case(
          context = NamingContext.Nested(
            NamingContext.Named("AutoOrManual"),
            NamingContext.Named("OneOf")
          ),
          model = enum
        ),
        // Order is important for deserialization,
        // order swapped compared to originally
        Model.Union.Case(
          context = NamingContext.Named("OneOf"),
          model = Primitive.String(default = null, description = null)
        )
      ),
      inline = listOf(
        Primitive.String(default = null, description = null),
        enum
      )
    )
    assertEquals(setOf(expected), actual)
  }
}
