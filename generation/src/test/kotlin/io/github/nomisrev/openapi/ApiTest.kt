@file:OptIn(ExperimentalCompilerApi::class)

package io.github.nomisrev.openapi

import com.tschuchort.compiletesting.JvmCompilationResult
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import io.ktor.http.*
import io.ktor.http.HttpMethod.Companion.Get
import kotlin.test.Test
import kotlin.test.assertEquals
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
                            Model.Primitive.String(null, null, TextConstraint.NONE),
                            isRequired = true,
                            isNullable = false,
                            description = null
                          )
                        ),
                        listOf(Model.Primitive.String(null, null, TextConstraint.NONE)),
                        ObjectConstraint.NONE
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

fun API.compiles(): JvmCompilationResult {
  val ctx = OpenAPIContext(GenerationConfig("", "", "io.test", "TestApi", true))
  val filesAsSources =
    with(ctx) {
      Root("TestApi", emptyList(), listOf(this@compiles)).toFileSpecs().map {
        SourceFile.kotlin("${it.name}.kt", it.asCode())
      }
    }
  val result =
    KotlinCompilation()
      .apply {
        val predef = SourceFile.kotlin("Predef.kt", with(ctx) { predef() }.asCode())
        sources = filesAsSources + predef
        inheritClassPath = true
        messageOutputStream = System.out
      }
      .compile()
  assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK)
  return result
}
