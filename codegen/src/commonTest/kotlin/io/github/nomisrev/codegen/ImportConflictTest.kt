package io.github.nomisrev.codegen

import io.github.nomisrev.codegen.emit.emitFile
import io.github.nomisrev.codegen.ir.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ImportConflictTest {
  @Test
  fun conflict_on_simple_name_prefers_fqn() {
    val file =
      KtFile(
        name = "Conflicts.kt",
        pkg = "com.example",
        declarations =
          listOf(
            KtClass(
              name = "Holder",
              kind = KtClassKind.Class,
              properties =
                listOf(
                  KtProperty("a", KtType.Simple("a.b.C")),
                  KtProperty("b", KtType.Simple("x.y.C")),
                ),
            )
          ),
      )

    val expected =
      """
      package com.example

      class Holder {
          val a: a.b.C
          val b: x.y.C
      }
      
      """
        .trimIndent()

    val actual = emitFile(file)
    assertEquals(expected, actual)
  }
}
