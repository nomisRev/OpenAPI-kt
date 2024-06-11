// package io.github.nomisrev.openapi
//
// import io.github.nomisrev.openapi.Parameter.Input.Query
// import io.github.nomisrev.openapi.ReferenceOr.Value
// import io.github.nomisrev.openapi.Schema.Type
// import io.github.nomisrev.openapi.test.KModel
// import io.github.nomisrev.openapi.test.KModel.Primitive.String
// import io.github.nomisrev.openapi.test.models
// import io.github.nomisrev.openapi.test.template
// import io.github.nomisrev.openapi.test.toCode
// import kotlin.test.Test
// import kotlin.test.assertTrue
//
// class InlineSchemaTest {
//  val name = "name" to Value(Schema(nullable = false, type = Type.Basic.String, description =
// "test"))
//  val age = "age" to Value(Schema(type = Type.Basic.Integer, default = ExampleValue.Single("1")))
//  val enum = "enum" to Value(Schema(type = Type.Basic.String, enum = listOf("deceased", "alive")))
//  val oneOf = "oneOf" to Value(
//    Schema(
//      oneOf = listOf(
//        Value(Schema(type = Type.Basic.String)),
//        ReferenceOr.schema("TopEnum")
//// Currently inline complex schemas not supported, only top-level and primitives
////    Value(Schema(type = Type.Basic.String, enum = listOf("deceased", "alive")))
////    Value(Schema(type = Type.Array(listOf(Type.Basic.String, Type.Basic.Integer))))
//      )
//    )
//  )
//  val anyOf = "anyOf" to Value(
//    Schema(
//      anyOf = listOf(
//        Value(Schema(type = Type.Basic.String)),
//        ReferenceOr.schema("TopEnum")
//// Currently inline complex schemas not supported, only top-level and primitives
////    Value(Schema(type = Type.Basic.String, enum = listOf("deceased", "alive")))
////    Value(Schema(type = Type.Array(listOf(Type.Basic.String, Type.Basic.Integer))))
//      )
//    )
//  )
//  val pet = "Pet" to Value(Schema(required = listOf("age"), properties = mapOf(name, age, enum,
// oneOf, anyOf)))
//  val pets = Value(Schema(type = Schema.Type.Basic.Array, items = pet.second))
//
//  val kenum = KModel.Enum("Enum", String, listOf("deceased", "alive"))
//  val koneOf = KModel.Union.OneOf(
//    "OneOf", listOf(
//      KModel.Primitive.String,
//      KModel.Enum("TopEnum", String, listOf("deceased", "alive"))
//    )
//  )
//  val kanyOf = KModel.Union.AnyOf(
//    "AnyOf", listOf(
//      KModel.Primitive.String,
//      KModel.Enum("TopEnum", String, listOf("deceased", "alive"))
//    )
//  )
//  val kpet = KModel.Object(
//    "Pet", null, listOf(
//      KModel.Object.Property(
//        "name", String,
//        isRequired = false,
//        isNullable = false,
//        description = "test",
//        defaultValue = null
//      ),
//      KModel.Object.Property(
//        "age", KModel.Primitive.Int,
//        isRequired = true,
//        isNullable = true,
//        description = null,
//        defaultValue = "1"
//      ),
//      KModel.Object.Property("enum", kenum, false, true, null, null),
//      KModel.Object.Property("oneOf", koneOf, false, true, null, null),
//      KModel.Object.Property("anyOf", kanyOf, false, true, null, null)
//    ), listOf(kenum, koneOf, kanyOf)
//  )
//  val mediaType =
//    mapOf(applicationJson to MediaType(Value(Schema(type = Schema.Type.Basic.Array, items =
// pet.second))))
//
//  @Test
//  fun toplevelSchema() {
//    val actual = OpenAPI(
//      info = Info(title = "Test Spec", version = "1.0"),
//      components = Components(
//        schemas = mapOf(
//          "Pet" to Value(Schema(required = listOf("age"), properties = mapOf(name, age, enum,
// oneOf, anyOf))),
//          "TopEnum" to enum.second
//        )
//      )
//    ).models()
//    val models = setOf(kpet, kenum.copy(simpleName = "TopEnum"))
//    assertTrue(models == actual)
//  }
//
//  @Test
//  fun operationResponse() {
//    val actual = OpenAPI(
//      info = Info(title = "Test Spec", version = "1.0"),
//      paths = mapOf(
//        "/pets" to PathItem(
//          get = Operation(
//            operationId = "listPets",
//            responses = Responses(200 to Value(Response(content = mediaType)))
//          )
//        )
//      ),
//      components = Components(schemas = mapOf("TopEnum" to enum.second))
//    ).models()
//    val expected = setOf(
//      KModel.Object(
//        "ListPetsResponse", null, listOf(
//          KModel.Object.Property(
//            "name", String,
//            isRequired = false,
//            isNullable = false,
//            description = "test",
//            defaultValue = null
//          ),
//          KModel.Object.Property(
//            "age", KModel.Primitive.Int,
//            isRequired = true,
//            isNullable = true,
//            description = null,
//            defaultValue = "1"
//          ),
//          KModel.Object.Property("enum", kenum, false, true, null, null),
//          KModel.Object.Property("oneOf", koneOf, false, true, null, null),
//          KModel.Object.Property("anyOf", kanyOf, false, true, null, null)
//        ), listOf(kenum, koneOf, kanyOf)
//      ),
//      kenum.copy(simpleName = "TopEnum")
//    )
//    assertTrue(expected == actual)
//  }
//
//  @Test
//  fun operationBody() {
//    val actual = OpenAPI(
//      info = Info(title = "Test Spec", version = "1.0"),
//      paths = mapOf(
//        "/pets" to PathItem(
//          get = Operation(
//            operationId = "listPets",
//            parameters = listOf(Value(Parameter(name = "limit", input = Query))),
//            requestBody = Value(RequestBody(content = mediaType)),
//            responses = Responses(200, Response())
//          )
//        )
//      ),
//      components = Components(schemas = mapOf("TopEnum" to enum.second))
//    ).models()
//    val expected = setOf(
//      KModel.Object(
//        "ListPetsRequest", null, listOf(
//          KModel.Object.Property(
//            "name", String,
//            isRequired = false,
//            isNullable = false,
//            description = "test",
//            defaultValue = null
//          ),
//          KModel.Object.Property(
//            "age", KModel.Primitive.Int,
//            isRequired = true,
//            isNullable = true,
//            description = null,
//            defaultValue = "1"
//          ),
//          KModel.Object.Property("enum", kenum, false, true, null, null),
//          KModel.Object.Property("oneOf", koneOf, false, true, null, null),
//          KModel.Object.Property("anyOf", kanyOf, false, true, null, null)
//        ), listOf(kenum, koneOf, kanyOf)
//      ),
//      KModel.Primitive.Unit,
//      kenum.copy(simpleName = "TopEnum")
//    )
//    assertTrue(expected == actual)
//  }
//
//  @Test
//  fun operationRefBody() {
//    val actual = OpenAPI(
//      info = Info(title = "Test Spec", version = "1.0"),
//      paths = mapOf(
//        "/pets" to PathItem(
//          get = Operation(
//            operationId = "listPets",
//            parameters = listOf(Value(Parameter(name = "limit", input = Query))),
//            requestBody = Value(
//              RequestBody(content = mapOf(applicationJson to
// MediaType(ReferenceOr.schema("Pets"))))
//            ),
//            responses = Responses(200, Response())
//          )
//        )
//      ),
//      components = Components(
//        schemas = mapOf(
//          "Pets" to Value(Schema(type = Schema.Type.Basic.Object, properties = mapOf("value" to
// pets))),
//          "TopEnum" to enum.second
//        )
//      )
//    ).models()
//    val expected = setOf(
//      KModel.Primitive.Unit,
//      KModel.Object(
//        "Pets", null, listOf(
//          KModel.Object.Property(
//            "value", KModel.Collection.List(kpet.copy(simpleName = "value")),
//            isRequired = false,
//            isNullable = true,
//            description = null,
//            defaultValue = null
//          ),
//        ), listOf(kpet.copy(simpleName = "value"))
//      ),
//      kenum.copy(simpleName = "TopEnum")
//    )
//
//    assertTrue(expected == actual)
//  }
//
//  @Test
//  fun test() {
//    val actual = OpenAPI(
//      info = Info(title = "Test Spec", version = "1.0"),
//      components = Components(
//        schemas = mapOf(
//          "Pet" to Value(Schema(required = listOf("age"), properties = mapOf(name, age, enum,
// oneOf, anyOf))),
//          "TopEnum" to enum.second
//        )
//      )
//    ).models().first()
//    println(template { toCode(actual) })
//  }
// }
