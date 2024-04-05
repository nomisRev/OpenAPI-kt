package io.github.nomisrev.openapi

import com.goncalossilva.resources.Resource
import io.github.nomisrev.openapi.OpenAPI.Companion.JSON_FLEXIBLE
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class OpenAPISerializerEnd2EndTest {

  @Test
  fun petstoreJson() {
   JSON_FLEXIBLE.decodeFromString(
      OpenAPI.serializer(),
      Resource("src/commonTest/resources/petstore.json").readText(),
    )
  }

  @Test
  fun petstoreJsonIsomorphic() {
    val openAPI = JSON_FLEXIBLE.decodeFromString(
      OpenAPI.serializer(),
      Resource("src/commonTest/resources/petstore.json").readText(),
    )
    val json = JSON_FLEXIBLE.encodeToString(OpenAPI.serializer(), openAPI)
    assertEquals(
      openAPI,
      JSON_FLEXIBLE.decodeFromString(OpenAPI.serializer(), json)
    )
  }

  @Test
  fun petStoreMore() {
    JSON_FLEXIBLE.decodeFromString(
      OpenAPI.serializer(),
      Resource("src/commonTest/resources/petstore_more.json").readText(),
    )
  }

  @Test
  fun issue1801() {
    JSON_FLEXIBLE.decodeFromString(
      OpenAPI.serializer(),
      Resource("src/commonTest/resources/issue-1801.json").readText(),
    )
  }

  @Test
  @Ignore
  // TODO check https://github.com/swagger-api/swagger-parser/issues/1821
  fun issue1821() {
    JSON_FLEXIBLE.decodeFromString(
      OpenAPI.serializer(),
      Resource("src/commonTest/resources/issue-1821.json").readText(),
    )
  }

  @Test
  fun issue1975() {
    JSON_FLEXIBLE.decodeFromString(
      OpenAPI.serializer(),
      Resource("src/commonTest/resources/issue-1975.json").readText(),
    )
  }

  @Test
  fun oas31() {
    JSON_FLEXIBLE.decodeFromString(
      OpenAPI.serializer(),
      Resource("src/commonTest/resources/oas_3_1_0.json").readText(),
    )
  }

  @Test
  fun basic() {
    JSON_FLEXIBLE.decodeFromString(
      OpenAPI.serializer(),
      Resource("src/commonTest/resources/basic.json").readText(),
    )
  }

  @Test
  fun schemaSiblings() {
    JSON_FLEXIBLE.decodeFromString(
      OpenAPI.serializer(),
      Resource("src/commonTest/resources/schemaSiblings.json").readText(),
    )
  }

  @Test
  fun securitySchemes() {
    JSON_FLEXIBLE.decodeFromString(
      OpenAPI.serializer(),
      Resource("src/commonTest/resources/securitySchemes_3_1_0.json").readText(),
    )
  }
}
