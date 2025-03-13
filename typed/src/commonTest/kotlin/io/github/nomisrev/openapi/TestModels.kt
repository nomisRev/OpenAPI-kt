package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.Model.Object.Property
import io.github.nomisrev.openapi.Model.Primitive
import io.github.nomisrev.openapi.ReferenceOr.Companion.value
import io.github.nomisrev.openapi.Schema.Type

val idSchema =
  Schema(
    type = Type.Basic.Object,
    description = value("An explicit ID type"),
    properties = mapOf("value" to value(Schema(type = Type.Basic.String)))
  )

val personSchema =
  Schema(
    type = Type.Basic.Object,
    properties =
      mapOf(
        "id" to ReferenceOr.schema("Id"),
        "name" to
          value(
            Schema(
              type = Type.Basic.String,
              nullable = false,
              description = value("The name of the person"),
              default = ExampleValue("John Doe")
            )
          ),
        "age" to value(Schema(type = Type.Basic.Integer)),
      ),
    description = value("A person"),
    required = listOf("id", "name")
  )

val id =
  Model.obj(
    context = NamingContext.Named("Id"),
    description = "An explicit ID type",
    properties =
      listOf(
        Property(
          "value",
          Primitive.string(),
          isRequired = false,
          isNullable = true,
          description = null
        )
      ),
    inline = listOf(Primitive.string())
  )

val testAPI = OpenAPI(info = Info("Test API", version = "1.0.0"))

val enumSchema: Schema =
  Schema(type = Type.Basic.String, enum = listOf("Auto", "Manual"), default = ExampleValue("Auto"))

val enum =
  Model.Enum.Closed(
    context =
      NamingContext.Nested(NamingContext.Named("AutoOrManual"), NamingContext.Named("OneOf")),
    inner = Primitive.string(default = "Auto", description = null),
    values = listOf("Auto", "Manual"),
    default = "Auto",
    description = null
  )
