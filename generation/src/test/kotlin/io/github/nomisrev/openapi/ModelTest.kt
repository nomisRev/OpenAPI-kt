@file:OptIn(ExperimentalCompilerApi::class)

package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.FileSpec
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi

class ModelTest {
  @Test
  fun dataClass() {
    val code =
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
        )
        .compiles()
    assertFalse(code.contains("requireAll"))
    assertFalse(code.contains("require"))
  }

  @Test
  fun union() {
    Model.Union(
        NamingContext.Named("IntOrString"),
        listOf(
          Model.Union.Case(
            NamingContext.Named("IntOrString"),
            Model.Primitive.Int(null, null, null)
          ),
          Model.Union.Case(
            NamingContext.Named("IntOrString"),
            Model.Primitive.String(null, null, null)
          )
        ),
        null,
        null,
        listOf(Model.Primitive.String(null, null, null), Model.Primitive.String(null, null, null)),
      )
      .compiles()
  }

  @Test
  fun enum() {
    Model.Enum.Closed(
        NamingContext.Named("AutoOrManual"),
        Model.Primitive.String(null, null, null),
        listOf("auto", "manual"),
        "auto",
        null
      )
      .compiles()
  }

  @Test
  fun openEnum() {
    Model.Enum.Open(NamingContext.Named("AutoOrManual"), listOf("auto", "manual"), "auto", null)
      .compiles()
  }
}

fun Model.compiles(): String {
  val ctx = OpenAPIContext(GenerationConfig("", "", "io.test", "TestApi", true))
  val file =
    with(ctx) {
      requireNotNull(toFileSpecOrNull()) { "No File was generated for ${this@compiles}" }
    }
  val code = file.asCode()
  val source = SourceFile.kotlin("${file.name}.kt", file.asCode())
  val result =
    KotlinCompilation()
      .apply {
        val predef = SourceFile.kotlin("Predef.kt", with(ctx) { predef() }.asCode())
        sources = listOf(source, predef)
        inheritClassPath = true
        messageOutputStream = System.out
      }
      .compile()
  assertEquals(result.exitCode, KotlinCompilation.ExitCode.OK, code)
  return code
}

fun FileSpec.asCode(): String = buildString { writeTo(this) }
