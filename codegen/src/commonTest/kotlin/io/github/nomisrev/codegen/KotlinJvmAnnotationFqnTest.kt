package io.github.nomisrev.codegen

import io.github.nomisrev.codegen.emit.emitFile
import io.github.nomisrev.codegen.ir.*
import kotlin.test.Test
import kotlin.test.assertEquals

class KotlinJvmAnnotationFqnTest {
  @Test
  fun kotlin_jvm_annotations_are_rendered_as_fqn_when_not_explicitly_imported() {
    val file =
      KtFile(
        name = "JvmAnn.kt",
        pkg = "com.example",
        declarations =
          listOf(
            KtClass(
              name = "ID",
              kind = KtClassKind.Class,
              modifiers = listOf(KtModifier.Value),
              annotations = listOf(KtAnnotation(KtType.Simple("kotlin.jvm.JvmInline"))),
            ),
            KtFunction(
              name = "use",
              params =
                listOf(
                  KtParam(
                    name = "x",
                    type = KtType.Simple("kotlin.Int"),
                    annotations =
                      listOf(KtAnnotation(KtType.Simple("kotlin.jvm.JvmSuppressWildcards"))),
                  )
                ),
              returnType = KtType.Simple("kotlin.Unit"),
            ),
          ),
      )

    val expected =
      """
      package com.example

      import kotlin.jvm.JvmSuppressWildcards
      import kotlin.jvm.JvmInline

      @JvmInline
      value class ID

      fun use(@JvmSuppressWildcards x: Int): Unit
      
      """
        .trimIndent()

    assertEquals(expected, emitFile(file))
  }
}
