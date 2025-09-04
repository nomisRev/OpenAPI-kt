@file:OptIn(org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi::class)

package io.github.nomisrev.codegen

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import io.github.nomisrev.codegen.emit.emitFile
import io.github.nomisrev.codegen.transform.toIrFile
import io.github.nomisrev.openapi.GenerationConfig
import io.github.nomisrev.openapi.OpenAPI
import io.github.nomisrev.openapi.OpenAPIContext
import io.github.nomisrev.openapi.models
import io.github.nomisrev.openapi.predef
import io.github.nomisrev.openapi.root
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class YouTrackCompilationTest {
  @Test
  fun youtrack_json_generates_code_that_compiles() {
    // Locate youtrack.json without duplicating it as a resource
    val candidates =
      listOf(
        Paths.get("youtrack.json"), // working directory at repository root
        Paths.get("../youtrack.json"), // working directory at module dir
      )
    val path: Path =
      candidates.firstOrNull { Files.exists(it) }
        ?: fail(
          "Could not find youtrack.json in repository root. Checked: ${candidates.joinToString()} "
        )

    val json = Files.readString(path)
    val openApi = OpenAPI.fromJson(json)

    // Prepare model and API IR
    val models = openApi.models()
    val root = openApi.root(openApi.info.title)
    val pkg = "com.example.youtrack"

    val modelIr = models.toIrFile(fileName = "Models.kt", pkg = pkg)
    // Emit Kotlin sources for models only (integration scope)
    val sources = mutableListOf<SourceFile>()
    sources += SourceFile.kotlin(modelIr.name, emitFile(modelIr))

    // Add Predef.kt required helpers from generation module
    val predef =
      OpenAPIContext(
        GenerationConfig(path = "", output = "", `package` = pkg, name = root.name, isK2 = true)
      ) {
        predef()
      }
    sources += SourceFile.kotlin("Predef.kt", predef.asCode())

    // Compile all sources
    val result =
      KotlinCompilation()
        .apply {
          this.sources = sources
          inheritClassPath = true
          messageOutputStream = System.out
        }
        .compile()

    assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode, result.messages)
  }
}

// Helper to turn KotlinPoet FileSpec into source String
private fun com.squareup.kotlinpoet.FileSpec.asCode(): String = buildString { writeTo(this) }
