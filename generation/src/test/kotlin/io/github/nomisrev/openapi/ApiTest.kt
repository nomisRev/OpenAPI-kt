@file:OptIn(ExperimentalCompilerApi::class)

package io.github.nomisrev.openapi

import io.ktor.http.*
import io.ktor.http.HttpMethod.Companion.Get
import kotlin.test.Test
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi

class ApiTest {
  @Test
  fun route() {
    API(
        name = "TestApi",
        routes =
          listOf(
            Route(
              operationId = "get",
              summary = "Get a user",
              path = "/user/{id}",
              method = Get,
              body =
                Route.Bodies(
                  false,
                  mapOf(
                    io.ktor.http.ContentType.Application.Json to
                      Route.Body.Json.FreeForm("A user", emptyMap())
                  ),
                  emptyMap()
                ),
              input = emptyList(),
              returnType =
                Route.Returns(
                  HttpStatusCode.OK to
                    Route.ReturnType(
                      Model.Object(
                        NamingContext.Named("User"),
                        null,
                        listOf(
                          Model.Object.Property(
                            "id",
                            Model.Primitive.String(null, null, null),
                            isRequired = true,
                            isNullable = false,
                            description = null
                          )
                        ),
                        listOf(Model.Primitive.String(null, null, null))
                      ),
                      emptyMap()
                    )
                ),
              extensions = emptyMap(),
              nested = emptyList()
            )
          ),
        nested = emptyList()
      )
      .compiles()
  }
}
