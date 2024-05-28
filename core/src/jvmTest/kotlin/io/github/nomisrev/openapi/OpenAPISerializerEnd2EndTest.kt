//package io.github.nomisrev.openapi
//
//import io.github.nomisrev.openapi.OpenAPI.Companion.Json
//import java.io.BufferedReader
//import kotlin.test.Ignore
//import kotlin.test.Test
//import kotlin.test.assertEquals
//
//class OpenAPISerializerEnd2EndTest {
//
//  fun resourceText(path: String): String =
//    requireNotNull(
//      OpenAPISerializerEnd2EndTest::class.java.classLoader
//        .getResourceAsStream(path)
//        ?.bufferedReader()
//        ?.use(BufferedReader::readText)
//    ) { "Resource not found: $path" }
//
//  @Test
//  fun petstoreJson() {
//    Json.decodeFromString(
//      OpenAPI.serializer(),
//      resourceText("petstore.json"),
//    )
//  }
//
//  @Test
//  // TODO Has default response, without default key??
//  @Ignore
//  fun petstoreJsonIsomorphic() {
//    val openAPI = Json.decodeFromString(
//      OpenAPI.serializer(),
//      resourceText("petstore.json"),
//    )
//    val json = Json.encodeToString(OpenAPI.serializer(), openAPI)
//    assertEquals(
//      openAPI,
//      Json.decodeFromString(OpenAPI.serializer(), json)
//    )
//  }
//
//  @Test
//  fun petStoreMore() {
//    Json.decodeFromString(
//      OpenAPI.serializer(),
//      resourceText("petstore_more.json"),
//    )
//  }
//
//  @Test
//  fun issue1801() {
//    Json.decodeFromString(
//      OpenAPI.serializer(),
//      resourceText("issue-1801.json"),
//    )
//  }
//
//  @Test
//  @Ignore
//  // TODO check https://github.com/swagger-api/swagger-parser/issues/1821
//  fun issue1821() {
//    Json.decodeFromString(
//      OpenAPI.serializer(),
//      resourceText("issue-1821.json"),
//    )
//  }
//
//  @Test
//  fun issue1975() {
//    Json.decodeFromString(
//      OpenAPI.serializer(),
//      resourceText("issue-1975.json"),
//    )
//  }
//
//  @Test
//  fun oas31() {
//    Json.decodeFromString(
//      OpenAPI.serializer(),
//      resourceText("oas_3_1_0.json"),
//    )
//  }
//
//  @Test
//  fun basic() {
//    Json.decodeFromString(
//      OpenAPI.serializer(),
//      resourceText("basic.json"),
//    )
//  }
//
//  @Test
//  fun schemaSiblings() {
//    Json.decodeFromString(
//      OpenAPI.serializer(),
//      resourceText("schemaSiblings.json"),
//    )
//  }
//
//  @Test
//  fun securitySchemes() {
//    Json.decodeFromString(
//      OpenAPI.serializer(),
//      resourceText("securitySchemes_3_1_0.json"),
//    )
//  }
//}
