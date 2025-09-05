package io.github.nomisrev.codegen.golden

import io.github.nomisrev.codegen.emit.emitFile
import io.github.nomisrev.codegen.ir.*
import kotlin.test.Test
import kotlin.test.assertEquals

class TypeAliasTest {
  @Test
  fun typealias_to_generic_type() {
    val file =
      KtFile(
        name = "Aliases.kt",
        pkg = "com.example",
        declarations =
          listOf(
            KtTypeAlias(
              name = "Headers",
              type =
                KtType.Generic(
                  raw = KtType.Simple("kotlin.collections.Map"),
                  args =
                    listOf(
                      KtType.Simple("kotlin.String"),
                      KtType.Simple("kotlin.Int", nullable = true),
                    ),
                ),
            )
          ),
      )

    val expected =
      """
      package com.example

      typealias Headers = Map<String, Int?>
      """
        .trimIndent() + "\n"

    val actual = emitFile(file)
    assertEquals(expected, actual)
  }
}
