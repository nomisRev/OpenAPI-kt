package io.github.nomisrev.codegen.emit

import io.github.nomisrev.codegen.format.IndentWriter
import io.github.nomisrev.codegen.ir.KtKDoc

internal fun IndentWriter.renderKDoc(kdoc: KtKDoc?) {
  if (kdoc == null) return
  append("/**").newline()
  for (line in kdoc.lines) {
    append(" * ").append(line).newline()
  }
  append(" */").newline()
}
