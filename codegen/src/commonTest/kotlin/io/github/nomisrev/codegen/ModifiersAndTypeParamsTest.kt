package io.github.nomisrev.codegen

import io.github.nomisrev.codegen.emit.emitFile
import io.github.nomisrev.codegen.ir.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ModifiersAndTypeParamsTest {
  @Test
  fun render_modifiers_for_class_property_and_function_and_typeparams() {
    val file =
      KtFile(
        name = "ModsAndTP.kt",
        pkg = "com.example",
        declarations =
          listOf(
            // Class with many modifiers
            KtClass(
              name = "Modded",
              kind = KtClassKind.Class,
              modifiers =
                listOf(
                  KtModifier.Inline,
                  KtModifier.Value,
                  KtModifier.Sealed,
                  KtModifier.Open,
                  KtModifier.Final,
                  KtModifier.Abstract,
                ),
            ),
            // Top-level properties to hit lateinit/const
            KtProperty(
              name = "p",
              type = KtType.Simple("kotlin.Int"),
              mutable = true,
              modifiers = listOf(KtModifier.Lateinit),
            ),
            KtProperty(
              name = "C",
              type = KtType.Simple("kotlin.Int"),
              modifiers = listOf(KtModifier.Const),
              initializer = KtExpr("1"),
            ),
            // Function with type params (reified, variance, multi-bounds) and override
            KtFunction(
              name = "f",
              params = emptyList(),
              returnType = KtType.Simple("kotlin.Unit"),
              typeParams =
                listOf(
                  KtTypeParam(
                    name = "T",
                    reified = true,
                    variance = KtVariance.Out,
                    bounds = listOf(KtType.Simple("com.a.A"), KtType.Simple("com.b.B")),
                  ),
                  KtTypeParam(
                    name = "U",
                    variance = KtVariance.In,
                    bounds = listOf(KtType.Simple("com.c.C")),
                  ),
                  KtTypeParam(name = "V"),
                ),
              modifiers = listOf(KtModifier.Override),
            ),
          ),
      )

    val expected =
      """
      package com.example

      inline value sealed open final abstract class Modded
      
      lateinit var p: Int
      
      const val C: Int = 1
      
      override fun <reified out T : com.a.A & com.b.B, in U : com.c.C, V> f(): Unit
      
      """
        .trimIndent()

    assertEquals(expected, emitFile(file))
  }
}
