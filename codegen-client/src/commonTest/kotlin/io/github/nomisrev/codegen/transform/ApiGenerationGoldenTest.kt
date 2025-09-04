package io.github.nomisrev.codegen.transform

import io.github.nomisrev.codegen.emit.emitFile
import io.github.nomisrev.openapi.API
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import io.github.nomisrev.openapi.Parameter
import io.github.nomisrev.openapi.Root
import io.github.nomisrev.openapi.Route
import io.github.nomisrev.openapi.Route.Bodies
import io.github.nomisrev.openapi.Route.Body
import io.github.nomisrev.openapi.Route.ReturnType
import io.github.nomisrev.openapi.Route.Returns
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlin.test.Test
import kotlin.test.assertEquals

class ApiGenerationGoldenTest {
  private fun registryFor(vararg models: Model): ModelRegistry = ModelRegistry.from(models.toList())

  @Test
  fun simple_get_with_path_param_and_no_body() {
    // Model returned by the route
    val user =
      Model.Object(
        context = NamingContext.Named("User"),
        description = null,
        properties =
          listOf(
            Model.Object.Property(
              baseName = "id",
              model = Model.Primitive.String(default = null, description = null, constraint = null),
              isRequired = true,
              isNullable = false,
              description = null,
            )
          ),
        inline = emptyList(),
      )

    val route =
      Route(
        operationId = "getUser",
        summary = "Get a user by id",
        path = "/users/{id}",
        method = HttpMethod.Get,
        body = Bodies(required = false, types = emptyMap(), extensions = emptyMap()),
        input =
          listOf(
            Route.Input(
              name = "id",
              type = Model.Primitive.String(default = null, description = null, constraint = null),
              isRequired = true,
              input = Parameter.Input.Path,
              description = null,
            )
          ),
        returnType =
          Returns(
            mapOf(HttpStatusCode.OK to ReturnType(user, extensions = emptyMap())),
            extensions = emptyMap(),
          ),
        extensions = emptyMap(),
        nested = emptyList(),
      )

    val root =
      Root(
        name = "Example",
        operations = emptyList(),
        endpoints = listOf(API("users", routes = listOf(route), nested = emptyList())),
      )

    val ctx = ApiToIr.Ctx(pkg = "com.example", name = root.name, registry = registryFor(user))
    val files = ApiToIr.generate(root, ctx)

    // Expect two files: Users.kt and Example.kt (root aggregator)
    val usersFile = files.first { it.name == "Users.kt" }
    val actualUsers = emitFile(usersFile)

    val expectedUsers =
      """
      package com.example

      import io.ktor.client.HttpClient
      import io.ktor.client.call.body
      import io.ktor.client.request.HttpRequestBuilder
      import io.ktor.client.request.forms.formData
      import io.ktor.client.request.request
      import io.ktor.client.request.setBody
      import io.ktor.http.ContentType
      import io.ktor.http.HttpMethod
      import io.ktor.http.contentType
      import io.ktor.http.path

      interface Users {
          /**
           * Get a user by id
           */
          suspend fun getUser(id: String, configure: (HttpRequestBuilder) -> Unit = {}): User
      }

      fun Users(client: HttpClient): Users {
          return UsersKtor(client)
      }

      private class UsersKtor(val client: HttpClient): Users {
          /**
           * Get a user by id
           */
          suspend override fun getUser(id: String, configure: (HttpRequestBuilder) -> Unit): User {
              val response = client.request {
                  configure(this)
                  method = HttpMethod.Get
                  url { path("users", id) }
              }
              return response.body()
          }
      }
      
      """
        .trimIndent()

    assertEquals(expectedUsers, actualUsers)
  }

  @Test
  fun post_with_json_body_and_201_response() {
    val createReq =
      Model.Object(
        context = NamingContext.Named("CreateUserRequest"),
        description = null,
        properties =
          listOf(
            Model.Object.Property(
              baseName = "name",
              model = Model.Primitive.String(default = null, description = null, constraint = null),
              isRequired = true,
              isNullable = false,
              description = null,
            )
          ),
        inline = emptyList(),
      )
    val user =
      Model.Object(
        context = NamingContext.Named("User"),
        description = null,
        properties =
          listOf(
            Model.Object.Property(
              baseName = "id",
              model = Model.Primitive.String(default = null, description = null, constraint = null),
              isRequired = true,
              isNullable = false,
              description = null,
            )
          ),
        inline = emptyList(),
      )

    val route =
      Route(
        operationId = "createUser",
        summary = "Create a user",
        path = "/users",
        method = HttpMethod.Post,
        body =
          Bodies(
            required = true,
            types =
              mapOf(
                ContentType.Application.Json to
                  Body.Json.Defined(type = createReq, description = null, extensions = emptyMap())
              ),
            extensions = emptyMap(),
          ),
        input = emptyList(),
        returnType =
          Returns(
            mapOf(HttpStatusCode.Created to ReturnType(user, extensions = emptyMap())),
            extensions = emptyMap(),
          ),
        extensions = emptyMap(),
        nested = emptyList(),
      )

    val root =
      Root(
        name = "Example",
        operations = emptyList(),
        endpoints = listOf(API("users", routes = listOf(route), nested = emptyList())),
      )
    val registry = registryFor(createReq, user)
    val ctx = ApiToIr.Ctx(pkg = "com.example", name = root.name, registry = registry)
    val files = ApiToIr.generate(root, ctx)
    val usersFile = files.first { it.name == "Users.kt" }
    val actual = emitFile(usersFile)

    val expected =
      """
      package com.example

      import io.ktor.client.HttpClient
      import io.ktor.client.call.body
      import io.ktor.client.request.HttpRequestBuilder
      import io.ktor.client.request.forms.formData
      import io.ktor.client.request.request
      import io.ktor.client.request.setBody
      import io.ktor.http.ContentType
      import io.ktor.http.HttpMethod
      import io.ktor.http.contentType
      import io.ktor.http.path

      interface Users {
          /**
           * Create a user
           */
          suspend fun createUser(body: CreateUserRequest, configure: (HttpRequestBuilder) -> Unit = {}): User
      }

      fun Users(client: HttpClient): Users {
          return UsersKtor(client)
      }

      private class UsersKtor(val client: HttpClient): Users {
          /**
           * Create a user
           */
          suspend override fun createUser(body: CreateUserRequest, configure: (HttpRequestBuilder) -> Unit): User {
              val response = client.request {
                  configure(this)
                  method = HttpMethod.Post
                  url { path("/users") }
                  contentType(ContentType.Application.Json)
                  setBody(body)
              }
              return response.body()
          }
      }
      
      """
        .trimIndent()

    assertEquals(expected, actual)
  }
}
