package io.github.nomisrev.codegen

import io.github.nomisrev.codegen.emit.emitFile
import io.github.nomisrev.codegen.ir.*
import kotlin.test.Test
import kotlin.test.assertEquals

class InvalidIdentifierEscapingTest {
  @Test
  fun property_with_invalid_identifier_is_escaped() {
    val file =
      KtFile(
        name = "Esc2.kt",
        pkg = "com.example",
        declarations =
          listOf(
            KtClass(
              name = "K2",
              kind = KtClassKind.Class,
              properties = listOf(KtProperty(name = "foo-bar", type = KtType.Simple("kotlin.Int"))),
            )
          ),
      )

    val expected =
      """
      package com.example

      class K2 {
          val `foo-bar`: Int
      }
      
      """
        .trimIndent()

    assertEquals(expected, emitFile(file))
  }
}
