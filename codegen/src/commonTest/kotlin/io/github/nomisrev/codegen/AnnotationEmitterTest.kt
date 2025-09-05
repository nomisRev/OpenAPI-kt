package io.github.nomisrev.codegen

import io.github.nomisrev.codegen.emit.renderAnnotations
import io.github.nomisrev.codegen.format.IndentWriter
import io.github.nomisrev.codegen.ir.KtAnnotation
import io.github.nomisrev.codegen.ir.KtExpr
import io.github.nomisrev.codegen.ir.KtType
import io.github.nomisrev.codegen.util.ImportContext
import kotlin.test.Test
import kotlin.test.assertEquals

class AnnotationEmitterTest {
  @Test
  fun inline_multiple_annotations_with_named_and_positional_args() {
    val w = IndentWriter()
    val ctx =
      ImportContext(
        currentPackage = null,
        imported = listOf("kotlin.jvm.JvmInline", "com.x.MyAnn", "kotlin.Deprecated"),
      )

    val anns =
      listOf(
        KtAnnotation(KtType.Simple("kotlin.jvm.JvmInline")),
        KtAnnotation(KtType.Simple("kotlin.Deprecated"), mapOf("message" to KtExpr("\"old\""))),
        KtAnnotation(KtType.Simple("com.x.MyAnn"), mapOf(null to KtExpr("1"), "k" to KtExpr("42"))),
      )

    w.renderAnnotations(anns, ctx, inline = true)

    // inline mode separates by a single space
    assertEquals("@JvmInline @Deprecated(message = \"old\") @MyAnn(1, k = 42)", w.toString())
  }

  @Test
  fun non_inline_annotations_each_on_newline() {
    val w = IndentWriter()
    val ctx = ImportContext(currentPackage = null, imported = emptyList())

    val anns =
      listOf(
        KtAnnotation(KtType.Simple("kotlin.jvm.JvmStatic")),
        KtAnnotation(KtType.Simple("kotlin.Suppress"), mapOf(null to KtExpr("\"X\""))),
      )

    w.renderAnnotations(anns, ctx, inline = false)

    assertEquals("@kotlin.jvm.JvmStatic\n@Suppress(\"X\")\n", w.toString())
  }
}
