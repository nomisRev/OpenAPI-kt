package io.github.nomisrev.codegen.emit

import io.github.nomisrev.codegen.format.IndentWriter
import io.github.nomisrev.codegen.ir.KtType
import io.github.nomisrev.codegen.util.ImportContext

internal fun IndentWriter.renderType(type: KtType, ctx: ImportContext) {
  when (type) {
    is KtType.Simple -> {
      append(ctx.renderSimpleOrFqn(type.qualifiedName))
      if (type.nullable) append("?")
    }
    is KtType.Generic -> {
      renderType(type.raw, ctx)
      append("<")
      var first = true
      for (arg in type.args) {
        if (!first) append(", ") else first = false
        renderType(arg, ctx)
      }
      append(">")
      if (type.nullable) append("?")
    }
    is KtType.Function -> {
      append("(")
      var first = true
      for (p in type.params) {
        if (!first) append(", ") else first = false
        renderType(p, ctx)
      }
      append(") -> ")
      renderType(type.returnType, ctx)
      if (type.nullable) append("?")
    }
  }
}
