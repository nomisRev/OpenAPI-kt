package io.github.nomisrev.codegen.golden

import io.github.nomisrev.codegen.emit.emitFile
import io.github.nomisrev.codegen.ir.*
import kotlin.test.Test
import kotlin.test.assertEquals

class PackageAndClassTest {
  @Test
  fun file_with_package_and_simple_class_property() {
    val file =
      KtFile(
        name = "Models.kt",
        pkg = "com.example",
        declarations =
          listOf(
            KtClass(
              name = "Person",
              kind = KtClassKind.Class,
              properties = listOf(KtProperty(name = "name", type = KtType.Simple("kotlin.String"))),
            )
          ),
      )

    val expected =
      """
      package com.example

      class Person {
          val name: String
      }
      """
        .trimIndent() + "\n"

    val actual = emitFile(file)
    assertEquals(expected, actual)
  }
}
