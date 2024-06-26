package io.github.nomisrev.openapi

import java.io.BufferedReader
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json

class OpenAPIJsonEnd2EndTest {
  @Test
  fun petstoreJson() {
    OpenAPI.fromJson(resourceText("petstore.json"))
  }

  @Test
  // TODO Has default response, without default key??
  @Ignore
  fun petstoreJsonIsomorphic() {
    val openAPI = OpenAPI.fromJson(resourceText("petstore.json"))
    val json = Json.encodeToString(OpenAPI.serializer(), openAPI)
    assertEquals(openAPI, OpenAPI.fromJson(json))
  }

  @Test
  fun petStoreMore() {
    OpenAPI.fromJson(resourceText("petstore_more.json"))
  }

  @Test
  fun issue1801() {
    OpenAPI.fromJson(resourceText("issue-1801.json"))
  }

  @Test
  @Ignore
  // TODO check https://github.com/swagger-api/swagger-parser/issues/1821
  fun issue1821() {
    OpenAPI.fromJson(resourceText("issue-1821.json"))
  }

  @Test
  fun issue1975() {
    OpenAPI.fromJson(resourceText("issue-1975.json"))
  }

  @Test
  fun oas31() {
    OpenAPI.fromJson(resourceText("oas_3_1_0.json"))
  }

  @Test
  fun basic() {
    OpenAPI.fromJson(resourceText("basic.json"))
  }

  @Test
  fun schemaSiblings() {
    OpenAPI.fromJson(resourceText("schemaSiblings.json"))
  }

  @Test
  fun securitySchemes() {
    OpenAPI.fromJson(resourceText("securitySchemes_3_1_0.json"))
  }
}

fun resourceText(path: String): String =
  requireNotNull(
    OpenAPIJsonEnd2EndTest::class
      .java
      .classLoader
      .getResourceAsStream(path)
      ?.bufferedReader()
      ?.use(BufferedReader::readText)
  ) {
    "Resource not found: $path"
  }
