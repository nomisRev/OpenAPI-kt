@file:OptIn(org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi::class)

package io.github.nomisrev.codegen

import GenerationConfig
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import files
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.pathString
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

    val result =
      KotlinCompilation()
        .apply {
          this.sources = files(
            GenerationConfig(
              path.pathString,
              "OUTPUT_NOT_USED",
              "com.example.youtrack",
              "YouTrack"
            )
          ).map { (name, content) ->
            SourceFile.kotlin(name, content)
          }
          inheritClassPath = true
          messageOutputStream = System.out
        }
        .compile()

    assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode, result.messages)
  }
}

// Helper to turn KotlinPoet FileSpec into source String
private fun com.squareup.kotlinpoet.FileSpec.asCode(): String = buildString { writeTo(this) }
