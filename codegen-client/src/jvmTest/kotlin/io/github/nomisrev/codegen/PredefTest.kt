@file:OptIn(ExperimentalCompilerApi::class)

package io.github.nomisrev.codegen

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import predefContent
import kotlin.test.Test
import kotlin.test.assertEquals

class PredefTest {
  @Test
  fun compilePredef() {
    val result = KotlinCompilation()
      .apply {
        this.sources = listOf(
          SourceFile.kotlin(
            "predef.kt",
            predefContent("io.github.nomisrev.codegen")
          )
        )
        inheritClassPath = true
        messageOutputStream = System.out
      }.compile()

    assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode, result.messages)
  }
}