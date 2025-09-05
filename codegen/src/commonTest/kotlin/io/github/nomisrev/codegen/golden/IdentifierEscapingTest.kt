package io.github.nomisrev.codegen.golden

import io.github.nomisrev.codegen.emit.emitFile
import io.github.nomisrev.codegen.ir.*
import kotlin.test.Test
import kotlin.test.assertEquals

class IdentifierEscapingTest {
  @Test
  fun property_named_keyword_is_escaped() {
    val file =
      KtFile(
        name = "Esc.kt",
        pkg = "com.example",
        declarations =
          listOf(
            KtClass(
              name = "K",
              kind = KtClassKind.Class,
              properties =
                listOf(KtProperty(name = "object", type = KtType.Simple("kotlin.String"))),
            )
          ),
      )

    val expected =
      """
      package com.example

      class K {
          val `object`: String
      }
      """
        .trimIndent() + "\n"

    val actual = emitFile(file)
    assertEquals(expected, actual)
  }
}
