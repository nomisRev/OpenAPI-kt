package io.github.nomisrev.codegen

import io.github.nomisrev.codegen.emit.emitFile
import io.github.nomisrev.codegen.ir.*
import kotlin.test.Test
import kotlin.test.assertEquals

class CompanionObjectTest {
  @Test
  fun class_with_companion_properties_and_functions() {
    val companion =
      KtClass(
        name = "Companion",
        kind = KtClassKind.Object,
        properties = listOf(KtProperty(name = "VERSION", type = KtType.Simple("kotlin.String"))),
        functions =
          listOf(
            KtFunction(
              name = "create",
              params = emptyList(),
              returnType = KtType.Simple("kotlin.Int"),
              body = KtBlock("return 1"),
            )
          ),
      )

    val file =
      KtFile(
        name = "WithCompanion.kt",
        pkg = "com.example",
        declarations =
          listOf(KtClass(name = "WithCompanion", kind = KtClassKind.Class, companion = companion)),
      )

    assertEquals(
      """
      package com.example

      class WithCompanion {
          companion object {
              val VERSION: String
              fun create(): Int {
                  return 1
              }
          }
      }
      
      """
        .trimIndent(),
      emitFile(file),
    )
  }
}
