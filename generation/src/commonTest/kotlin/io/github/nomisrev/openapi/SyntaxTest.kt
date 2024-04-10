package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.Parameter.Input.Query
import io.github.nomisrev.openapi.ReferenceOr.Value
import io.github.nomisrev.openapi.Schema.Type
import io.github.nomisrev.openapi.test.KModel
import io.github.nomisrev.openapi.test.KModel.Primitive.String
import io.github.nomisrev.openapi.test.models
import kotlin.test.Test
import kotlin.test.assertTrue

class SyntaxTest {
  val name = "name" to Value(Schema(nullable = false, type = Type.Basic.String, description = "test"))
  val age = "age" to Value(Schema(type = Type.Basic.Integer, default = ExampleValue.Single("1")))
  val enum = "enum" to Value(Schema(type = Type.Basic.String, enum = listOf("deceased", "alive")))
  val oneOf = "oneOf" to Value(
    Schema(
      oneOf = listOf(
        Value(Schema(type = Type.Basic.String)),
        ReferenceOr.schema("TopEnum")
// Currently inline complex schemas not supported, only top-level and primitives
//    Value(Schema(type = Type.Basic.String, enum = listOf("deceased", "alive")))
//    Value(Schema(type = Type.Array(listOf(Type.Basic.String, Type.Basic.Integer))))
      )
    )
  )
  val anyOf = "anyOf" to Value(
    Schema(
      anyOf = listOf(
        Value(Schema(type = Type.Basic.String)),
        ReferenceOr.schema("TopEnum")
// Currently inline complex schemas not supported, only top-level and primitives
//    Value(Schema(type = Type.Basic.String, enum = listOf("deceased", "alive")))
//    Value(Schema(type = Type.Array(listOf(Type.Basic.String, Type.Basic.Integer))))
      )
    )
  )
  val pet = "Pet" to Value(Schema(required = listOf("age"), properties = mapOf(name, age, enum, oneOf, anyOf)))

  val kenum = KModel.Enum("Enum", String, listOf("deceased", "alive"))
  val koneOf = KModel.Union.OneOf(
    "OneOf", listOf(
      KModel.Primitive.String,
      KModel.Enum("TopEnum", String, listOf("deceased", "alive"))
    )
  )
  val kanyOf = KModel.Union.AnyOf(
    "AnyOf", listOf(
      KModel.Primitive.String,
      KModel.Enum("TopEnum", String, listOf("deceased", "alive"))
    )
  )
  val kpet = KModel.Object(
    "Pet", null, listOf(
      KModel.Object.Property(
        "name", String,
        isRequired = false,
        isNullable = false,
        description = "test",
        defaultValue = null
      ),
      KModel.Object.Property(
        "age", KModel.Primitive.Int,
        isRequired = true,
        isNullable = true,
        description = null,
        defaultValue = "1"
      ),
      KModel.Object.Property("enum", kenum, false, true, null, null),
      KModel.Object.Property("oneOf", koneOf, false, true, null, null),
      KModel.Object.Property("anyOf", kanyOf, false, true, null, null)
    ), listOf(kenum, koneOf, kanyOf)
  )

  /**
   * Test file with an object that holds:
   *   - an inline enum
   *   - an inline oneOf type
   */
  @Test
  fun schemaObjectWithAllInline() {
    val actual = OpenAPI(
      info = Info(title = "Test Spec", version = "1.0"),
      components = Components(
        schemas = mapOf(
          "Pet" to Value(Schema(required = listOf("age"), properties = mapOf(name, age, enum, oneOf, anyOf))),
          "TopEnum" to enum.second
        )
      )
    ).models()
    val models = listOf(kpet, kenum.copy(typeName = "TopEnum"))
    assertTrue(models == actual)
  }

  @Test
  fun operationInlineModel() {
    val actual = OpenAPI(
      info = Info(title = "Test Spec", version = "1.0"),
      paths = mapOf(
        "/pets" to PathItem(
          get = Operation(
            operationId = "listPets",
            parameters = listOf(Value(Parameter(name = "limit", input = Query))),
            responses = Responses(
              200 to Value(Response(content = mapOf(applicationJson to MediaType(pet.second))))
            )
          )
        )
      ),
      components = Components(schemas = mapOf("TopEnum" to enum.second))
    ).models()
    val expected = listOf(
      KModel.Object(
        "ListPetsRequest", null, listOf(
          KModel.Object.Property(
            "name", String,
            isRequired = false,
            isNullable = false,
            description = "test",
            defaultValue = null
          ),
          KModel.Object.Property(
            "age", KModel.Primitive.Int,
            isRequired = true,
            isNullable = true,
            description = null,
            defaultValue = "1"
          ),
          KModel.Object.Property("enum", kenum, false, true, null, null),
          KModel.Object.Property("oneOf", koneOf, false, true, null, null),
          KModel.Object.Property("anyOf", kanyOf, false, true, null, null)
        ), listOf(kenum, koneOf, kanyOf)
      ),
      kenum.copy(typeName = "TopEnum")
    )
    assertTrue(expected == actual)
  }
}