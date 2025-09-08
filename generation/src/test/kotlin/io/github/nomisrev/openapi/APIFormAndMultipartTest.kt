@file:OptIn(org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi::class)

package io.github.nomisrev.openapi

import io.ktor.http.HttpMethod
import kotlin.test.Test

class APIFormAndMultipartTest {
  @Test
  fun form_urlencoded_and_multipart_compile() {
    val formBodies = Route.Bodies(
      required = true,
      types = mapOf(
        "application/x-www-form-urlencoded" to Route.Body.FormUrlEncoded(
          parameters = listOf(
            Route.Body.Multipart.FormData("a", Model.Primitive.String(null, null, null)),
            Route.Body.Multipart.FormData("b", Model.Primitive.String(null, null, null))
          ),
          description = null,
          extensions = emptyMap()
        )
      ),
      extensions = emptyMap()
    )

    val multipartBodies = Route.Bodies(
      required = true,
      types = mapOf(
        "multipart/form-data" to Route.Body.Multipart.Value(
          parameters = listOf(
            Route.Body.Multipart.FormData("file", Model.Primitive.String(null, null, null)),
            Route.Body.Multipart.FormData("desc", Model.Primitive.String(null, null, null)),
          ),
          description = null,
          extensions = emptyMap()
        )
      ),
      extensions = emptyMap()
    )

    val api = API(
      name = "UploadApi",
      routes = listOf(
        Route(
          operationId = "submitForm",
          summary = "Submit form",
          path = "/submit",
          method = HttpMethod.Post,
          body = formBodies,
          input = emptyList(),
          returnType = Route.Returns(),
          extensions = emptyMap(),
          nested = emptyList(),
        ),
        Route(
          operationId = "upload",
          summary = "Upload file",
          path = "/upload/{id}",
          method = HttpMethod.Post,
          body = multipartBodies,
          input = listOf(Route.Input("id", Model.Primitive.String(null, null, null), isRequired = true, input = Parameter.Input.Path, description = null)),
          returnType = Route.Returns(),
          extensions = emptyMap(),
          nested = emptyList(),
        ),
      ),
      nested = emptyList(),
    )

    // Ensure code generation compiles for both kinds of bodies and path params
    api.compiles()
  }
}
