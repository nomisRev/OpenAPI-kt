package io.github.nomisrev.codegen

import io.github.nomisrev.codegen.emit.emitFile
import io.github.nomisrev.codegen.ir.*
import kotlin.test.Test
import kotlin.test.assertEquals

class CtorParamsAnnotationsAndDefaultsTest {
  @Test
  fun ctor_params_annotations_inline_and_defaults() {
    val file =
      KtFile(
        name = "Ctor.kt",
        pkg = "com.example",
        declarations =
          listOf(
            KtClass(
              name = "User",
              kind = KtClassKind.Class,
              primaryCtor =
                KtPrimaryConstructor(
                  params =
                    listOf(
                      KtParam(
                        name = "id",
                        type = KtType.Simple("kotlin.String"),
                        asProperty = true,
                        annotations =
                          listOf(
                            KtAnnotation(
                              KtType.Simple("kotlin.Deprecated"),
                              mapOf("message" to KtExpr("\"old\"")),
                            )
                          ),
                      ),
                      KtParam(
                        name = "age",
                        type = KtType.Simple("kotlin.Int"),
                        asProperty = true,
                        default = KtExpr("42"),
                      ),
                    )
                ),
            )
          ),
      )

    val expected =
      """
      package com.example

      class User(@Deprecated(message = "old") val id: String, val age: Int = 42)
      
      """
        .trimIndent()

    assertEquals(expected, emitFile(file))
  }
}
