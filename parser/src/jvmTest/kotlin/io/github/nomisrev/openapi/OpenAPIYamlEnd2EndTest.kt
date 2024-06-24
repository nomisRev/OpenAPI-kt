package io.github.nomisrev.openapi

import com.charleskorn.kaml.Yaml
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class OpenAPIYamlEnd2EndTest {

  @Test
  fun petstoreYaml() {
    OpenAPI.fromYaml(resourceText("petstore.yaml"))
  }

  @Test
  // TODO Has default response, without default key??
  @Ignore
  fun petstoreYamlIsomorphic() {
    val openAPI = OpenAPI.fromYaml(resourceText("petstore.yaml"))
    val yaml = Yaml.default.encodeToString(OpenAPI.serializer(), openAPI)
    assertEquals(openAPI, Yaml.default.decodeFromString(OpenAPI.serializer(), yaml))
  }

  @Test
  fun petStoreMore() {
    OpenAPI.fromYaml(resourceText("petstore_more.yaml"))
  }

  @Test
  fun issue934() {
    OpenAPI.fromYaml(resourceText("issue-934.yaml"))
  }

  @Test
  fun issue1070() {
    OpenAPI.fromYaml(resourceText("issue-1070.yaml"))
  }

  @Test
  fun issue1086() {
    OpenAPI.fromYaml(resourceText("issue-1086.yaml"))
  }
}
