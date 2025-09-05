package io.github.nomisrev.codegen.emit

import io.github.nomisrev.codegen.format.IndentWriter
import io.github.nomisrev.codegen.ir.KtAnnotation
import io.github.nomisrev.codegen.util.ImportContext
import io.github.nomisrev.codegen.util.escapeIfNeeded

internal fun IndentWriter.renderAnnotations(
  annotations: List<KtAnnotation>,
  ctx: ImportContext,
  inline: Boolean = false,
) {
  for ((index, ann) in annotations.withIndex()) {
    val name = ctx.renderSimpleOrFqn(ann.name.qualifiedName)
    append("@").append(name)
    if (ann.args.isNotEmpty()) {
      append("(")
      var first = true
      for ((k, v) in ann.args) {
        if (!first) append(", ") else first = false
        if (k != null) append(escapeIfNeeded(k)).append(" = ")
        append(v.text)
      }
      append(")")
    }
    if (inline) {
      if (index < annotations.size - 1) append(" ")
    } else {
      newline()
    }
  }
}
