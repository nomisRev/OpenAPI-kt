package io.github.nomisrev.codegen

import io.github.nomisrev.codegen.emit.emitFile
import io.github.nomisrev.codegen.ir.*
import kotlin.test.Test
import kotlin.test.assertEquals

class FunctionWithBodyTest {
  @Test
  fun function_with_params_annotations_and_body() {
    val file =
      KtFile(
        name = "Funcs.kt",
        pkg = "com.example",
        declarations =
          listOf(
            KtFunction(
              name = "sum",
              params =
                listOf(
                  KtParam("x", KtType.Simple("kotlin.Int")),
                  KtParam("y", KtType.Simple("kotlin.Int")),
                ),
              returnType = KtType.Simple("kotlin.Int"),
              body = KtBlock("return x + y"),
            )
          ),
      )

    val expected =
      """
      package com.example

      fun sum(x: Int, y: Int): Int {
          return x + y
      }
      
      """
        .trimIndent()

    val actual = emitFile(file)
    assertEquals(expected, actual)
  }
}
