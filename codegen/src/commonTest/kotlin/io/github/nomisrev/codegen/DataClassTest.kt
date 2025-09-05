package io.github.nomisrev.codegen

import io.github.nomisrev.codegen.emit.emitFile
import io.github.nomisrev.codegen.ir.*
import kotlin.test.Test
import kotlin.test.assertEquals

class DataClassTest {
  @Test
  fun data_class_with_ctor_defaults_and_kdoc() {
    val file =
      KtFile(
        name = "Models.kt",
        pkg = "com.example",
        declarations =
          listOf(
            KtClass(
              name = "User",
              kind = KtClassKind.Class,
              modifiers = listOf(KtModifier.Data),
              kdoc = KtKDoc(listOf("Represents a user")),
              primaryCtor =
                KtPrimaryConstructor(
                  params =
                    listOf(
                      KtParam(
                        name = "name",
                        type = KtType.Simple("kotlin.String"),
                        asProperty = true,
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

      /**
       * Represents a user
       */
      data class User(val name: String, val age: Int = 42)
      
      """
        .trimIndent()

    val actual = emitFile(file)
    assertEquals(expected, actual)
  }
}
