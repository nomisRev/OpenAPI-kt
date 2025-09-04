@file:OptIn(org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi::class)

package io.github.nomisrev.codegen

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import io.github.nomisrev.codegen.emit.generate
import io.github.nomisrev.codegen.transform.toIrFile
import io.github.nomisrev.openapi.Model
import io.github.nomisrev.openapi.NamingContext
import kotlin.test.Test
import kotlin.test.assertEquals
import predefContent

class UnionCompilationTest {
  @Test
  fun union_generation_compiles_without_unresolved_JvmInline_and_without_experimental_warnings() {
    // Build a minimal union with two primitive cases
    val union =
      Model.Union(
        context = NamingContext.Named("SampleUnion"),
        cases =
          listOf(
            Model.Union.Case(
              NamingContext.Named("CaseA"),
              Model.Primitive.String(null, null, null),
            ),
            Model.Union.Case(NamingContext.Named("CaseB"), Model.Primitive.Int(null, null, null)),
          ),
        default = null,
        description = null,
        inline = emptyList(),
      )

    val pkg = "com.example.union"
    val file = listOf<Model>(union).toIrFile(fileName = "Models.kt", pkg = pkg)
    val generated = generate(file)

    // Include Predef, which serializers depend on (attemptDeserialize, etc.)
    val predef = predefContent(pkg)

    val result =
      KotlinCompilation()
        .apply {
          sources =
            listOf(
              SourceFile.kotlin(generated.path.substringAfterLast('/'), generated.content),
              SourceFile.kotlin("Predef.kt", predef),
            )
          inheritClassPath = true
          allWarningsAsErrors = true
          messageOutputStream = System.out
        }
        .compile()

    assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode, result.messages)
    // Additionally, ensure no ExperimentalSerializationApi warnings slipped through
    // (compile-testing includes messages even on success)
    check(!result.messages.contains("ExperimentalSerializationApi")) {
      $"Unexpected ExperimentalSerializationApi warning in messages: ${result.messages}"
    }
  }
}
