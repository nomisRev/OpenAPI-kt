package io.github.nomisrev.codegen

import io.github.nomisrev.codegen.api.CodegenOptions
import io.github.nomisrev.codegen.emit.generate
import io.github.nomisrev.codegen.ir.KtFile
import kotlin.test.Test
import kotlin.test.assertEquals

class FileEmitterGenerateTest {
  @Test
  fun generate_path_with_and_without_package() {
    val fileWithPkg = KtFile(name = "Name.kt", pkg = "com.example")
    val fileNoPkg = KtFile(name = "Name.kt", pkg = null)

    val g1 = generate(fileWithPkg, CodegenOptions())
    val g2 = generate(fileNoPkg, CodegenOptions())

    assertEquals("com/example/Name.kt", g1.path)
    assertEquals("Name.kt", g2.path)
  }
}
