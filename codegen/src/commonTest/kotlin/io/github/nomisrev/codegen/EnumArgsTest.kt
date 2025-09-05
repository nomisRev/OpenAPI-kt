package io.github.nomisrev.codegen

import io.github.nomisrev.codegen.emit.emitFile
import io.github.nomisrev.codegen.ir.*
import kotlin.test.Test
import kotlin.test.assertEquals

class EnumArgsTest {
  @Test
  fun enum_entries_with_arguments_and_no_members() {
    val file =
      KtFile(
        name = "EnumsArgs.kt",
        pkg = "com.example",
        declarations =
          listOf(
            KtClass(
              name = "E",
              kind = KtClassKind.Enum,
              enumEntries =
                listOf(
                  KtEnumEntry("A", args = listOf(KtExpr("1"), KtExpr("\"x\""))),
                  KtEnumEntry("B"),
                ),
            )
          ),
      )

    val expected =
      """
      package com.example

      enum class E {
          A(1, "x"),
          B
      }
      
      """
        .trimIndent()

    assertEquals(expected, emitFile(file))
  }
}
