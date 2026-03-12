package io.github.nomisrev.openapi

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import kotlin.test.assertEquals
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi

/**
 * Compiles the given [KFile] list and asserts successful compilation.
 * Classpath is resolved from the current test classloader,
 * which includes ktor-client, serialization, and datetime via Gradle dependencies.
 */
@OptIn(ExperimentalCompilerApi::class)
fun assertCompiles(files: List<KFile>) {
    println("Compiling ${files.size} files: ${files.map { it.relativePath }}")
    val result = KotlinCompilation().apply {
        sources = files.map { SourceFile.kotlin(it.relativePath, it.content) }
        inheritClassPath = true  // inherit test runtime classpath (ktor, serialization, etc.)
        kotlincArguments = listOf(
            "-Xcontext-parameters",
            "-Xcontext-sensitive-resolution",
        )
    }.compile()
    if (result.exitCode != KotlinCompilation.ExitCode.OK) {
        error("Compilation failed:\n${result.messages}")
    }
    assertEquals(
        KotlinCompilation.ExitCode.OK,
        result.exitCode
    )
}
