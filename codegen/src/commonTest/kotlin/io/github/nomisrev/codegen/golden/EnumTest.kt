package io.github.nomisrev.codegen.golden

import io.github.nomisrev.codegen.emit.emitFile
import io.github.nomisrev.codegen.ir.*
import kotlin.test.Test
import kotlin.test.assertEquals

class EnumTest {
  @Test
  fun enum_with_entries_and_annotation() {
    val file =
      KtFile(
        name = "Enums.kt",
        pkg = "com.example",
        declarations =
          listOf(
            KtClass(
              name = "Role",
              kind = KtClassKind.Enum,
              annotations =
                listOf(
                  KtAnnotation(
                    name = KtType.Simple("kotlin.Deprecated"),
                    args = mapOf("message" to KtExpr("\"use new\"")),
                  )
                ),
              enumEntries = listOf(KtEnumEntry("ADMIN"), KtEnumEntry("USER")),
            )
          ),
      )

    val expected =
      """
      package com.example

      @Deprecated(message = "use new")
      enum class Role {
          ADMIN,
          USER
      }
      """
        .trimIndent() + "\n"

    val actual = emitFile(file)
    assertEquals(expected, actual)
  }
}
