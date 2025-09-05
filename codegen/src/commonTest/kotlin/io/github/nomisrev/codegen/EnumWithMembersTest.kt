package io.github.nomisrev.codegen

import io.github.nomisrev.codegen.emit.emitFile
import io.github.nomisrev.codegen.ir.*
import kotlin.test.Test
import kotlin.test.assertEquals

class EnumWithMembersTest {
  @Test
  fun enum_entries_followed_by_property_and_function_inserts_semicolon() {
    val file =
      KtFile(
        name = "Enums2.kt",
        pkg = "com.example",
        declarations =
          listOf(
            KtClass(
              name = "Status",
              kind = KtClassKind.Enum,
              enumEntries = listOf(KtEnumEntry("OPEN"), KtEnumEntry("CLOSED")),
              properties = listOf(KtProperty(name = "code", type = KtType.Simple("kotlin.Int"))),
              functions =
                listOf(
                  KtFunction(
                    name = "isOpen",
                    params = emptyList(),
                    returnType = KtType.Simple("kotlin.Boolean"),
                    body = KtBlock("return this == OPEN"),
                  )
                ),
            )
          ),
      )

    val expected =
      """
      package com.example

      enum class Status {
          OPEN,
          CLOSED;

          val code: Int
          fun isOpen(): Boolean {
              return this == OPEN
          }
      }
      
      """
        .trimIndent()
    assertEquals(expected, emitFile(file))
  }
}
