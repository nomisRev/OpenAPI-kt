package io.github.nomisrev.codegen

import io.github.nomisrev.codegen.emit.emitFile
import io.github.nomisrev.codegen.ir.*
import kotlin.test.Test
import kotlin.test.assertEquals

class TypeEmitterFunctionTypeTest {
  @Test
  fun function_type_with_nullable_and_generics() {
    val file =
      KtFile(
        name = "HigherOrder.kt",
        pkg = "com.example",
        declarations =
          listOf(
            KtFunction(
              name = "applyTwice",
              params =
                listOf(
                  KtParam(
                    name = "f",
                    type =
                      KtType.Function(
                        params =
                          listOf(
                            KtType.Simple("kotlin.Int"),
                            KtType.Generic(
                              raw = KtType.Simple("kotlin.collections.List"),
                              args = listOf(KtType.Simple("kotlin.String", nullable = true)),
                            ),
                          ),
                        returnType =
                          KtType.Generic(
                            raw = KtType.Simple("kotlin.collections.List"),
                            args =
                              listOf(
                                KtType.Generic(
                                  raw = KtType.Simple("kotlin.Pair"),
                                  args =
                                    listOf(
                                      KtType.Simple("kotlin.Int"),
                                      KtType.Simple("kotlin.String"),
                                    ),
                                  nullable = true,
                                )
                              ),
                            nullable = true,
                          ),
                      ),
                  )
                ),
              returnType = KtType.Simple("kotlin.Unit"),
            )
          ),
      )

    val expected =
      """
      package com.example

      fun applyTwice(f: (Int, List<String?>) -> List<Pair<Int, String>?>?): Unit
      
      """
        .trimIndent()

    assertEquals(expected, emitFile(file))
  }
}
