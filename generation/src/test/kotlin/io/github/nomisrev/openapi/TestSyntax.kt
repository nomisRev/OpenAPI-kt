@file:OptIn(ExperimentalCompilerApi::class)

package io.github.nomisrev.openapi

import com.squareup.kotlinpoet.FileSpec
import com.tschuchort.compiletesting.JvmCompilationResult
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import kotlin.test.assertEquals
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi

/** Check if every text in [texts] occurs only a single time in [this]. */
fun String.containsSingle(texts: List<String>): Boolean = texts.all(::containsSingle)

/** Check if [text] occurs only a single time in [this]. */
fun String.containsSingle(text: String): Boolean {
  val indexOf = indexOf(text)
  return indexOf != -1 && lastIndexOf(text) == indexOf
}

private fun FileSpec.asCode(): String = buildString { writeTo(this) }

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
