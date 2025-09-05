package io.github.nomisrev.codegen.emit

import io.github.nomisrev.codegen.format.IndentWriter
import io.github.nomisrev.codegen.ir.*
import io.github.nomisrev.codegen.util.ImportContext
import io.github.nomisrev.codegen.util.escapeIfNeeded

private fun IndentWriter.renderVisibility(vis: KtVisibility) {
  when (vis) {
    KtVisibility.Public -> {}
    KtVisibility.Internal -> append("internal ")
    KtVisibility.Private -> append("private ")
    KtVisibility.Protected -> append("protected ")
  }
}

private fun IndentWriter.renderModifiers(mods: List<KtModifier>) {
  // order is pre-defined by enum declaration order
  for (m in mods) when (m) {
    KtModifier.Data -> append("data ")
    KtModifier.Inline -> append("inline ")
    KtModifier.Value -> append("value ")
    KtModifier.Sealed -> append("sealed ")
    KtModifier.Open -> append("open ")
    KtModifier.Final -> append("final ")
    KtModifier.Abstract -> append("abstract ")
    KtModifier.Suspend -> append("suspend ")
    KtModifier.Override -> append("override ")
    KtModifier.Lateinit -> append("lateinit ")
    KtModifier.Const -> append("const ")
    KtModifier.Companion -> {} // handled separately for companion object
  }
}

private fun IndentWriter.renderTypeParams(params: List<KtTypeParam>, ctx: ImportContext) {
  if (params.isEmpty()) return
  append("<")
  var first = true
  for (p in params) {
    if (!first) append(", ") else first = false
    if (p.reified) append("reified ")
    when (p.variance) {
      KtVariance.In -> append("in ")
      KtVariance.Out -> append("out ")
      null -> {}
    }
    append(escapeIfNeeded(p.name))
    if (p.bounds.isNotEmpty()) {
      append(" : ")
      var fb = true
      for (b in p.bounds) {
        if (!fb) append(" & ") else fb = false
        renderType(b, ctx)
      }
    }
  }
  append(">")
}

private fun IndentWriter.renderCtorParams(params: List<KtParam>, ctx: ImportContext) {
  append("(")
  var first = true
  for (p in params) {
    if (!first) append(", ") else first = false
    // annotations on parameters
    renderAnnotations(p.annotations, ctx, inline = true)
    if (p.annotations.isNotEmpty()) append(" ")
    if (p.asProperty) append(if (p.mutable) "var " else "val ")
    append(escapeIfNeeded(p.name)).append(": ")
    renderType(p.type, ctx)
    p.default?.let { append(" = ").append(it.text) }
  }
  append(")")
}

internal fun IndentWriter.renderDeclaration(d: KtDeclaration, ctx: ImportContext) {
  when (d) {
    is KtTypeAlias -> renderTypeAlias(d, ctx)
    is KtClass -> renderClass(d, ctx)
    is KtFunction -> renderFunction(d, ctx)
    is KtProperty -> renderProperty(d, ctx)
    is KtEnumEntry -> {
      /* only appears inside enum body */
    }
  }
}

private fun IndentWriter.renderTypeAlias(d: KtTypeAlias, ctx: ImportContext) {
  renderKDoc(d.kdoc)
  renderAnnotations(d.annotations, ctx)
  renderModifiers(d.modifiers)
  append("typealias ").append(escapeIfNeeded(d.name)).append(" = ")
  renderType(d.type, ctx)
  newline()
}

private fun IndentWriter.renderClass(d: KtClass, ctx: ImportContext) {
  renderKDoc(d.kdoc)
  renderAnnotations(d.annotations, ctx)
  renderVisibility(d.visibility)
  renderModifiers(d.modifiers)
  when (d.kind) {
    KtClassKind.Class -> append("class ")
    KtClassKind.Interface -> append("interface ")
    KtClassKind.Object -> append("object ")
    KtClassKind.Enum -> append("enum class ")
  }
  append(escapeIfNeeded(d.name))

  renderTypeParams(d.typeParams, ctx)

  // Primary constructor
  d.primaryCtor?.let { ctor ->
    if (ctor.visibility != KtVisibility.Public) {
      append(" ")
      renderVisibility(ctor.visibility)
      append("constructor")
    }
    renderCtorParams(ctor.params, ctx)
  }

  // Supertypes
  if (d.superTypes.isNotEmpty()) {
    append(": ")
    var first = true
    for (st in d.superTypes) {
      if (!first) append(", ") else first = false
      renderType(st, ctx)
    }
  }

  // Body
  val hasBody =
    d.enumEntries.isNotEmpty() ||
      d.properties.isNotEmpty() ||
      d.functions.isNotEmpty() ||
      d.companion != null
  if (!hasBody) {
    newline()
    return
  }

  append(" {").newline()
  pushIndent()

  // Enum entries
  if (d.enumEntries.isNotEmpty()) {
    val hasMembersAfterEntries =
      d.properties.isNotEmpty() || d.functions.isNotEmpty() || d.companion != null
    for ((i, e) in d.enumEntries.withIndex()) {
      e.kdoc?.let { renderKDoc(it) }
      renderAnnotations(e.annotations, ctx)
      append(escapeIfNeeded(e.name))
      if (e.args.isNotEmpty()) {
        append("(")
        var first = true
        for (a in e.args) {
          if (!first) append(", ") else first = false
          append(a.text)
        }
        append(")")
      }
      val isLast = i == d.enumEntries.lastIndex
      if (!isLast) {
        append(",").newline()
      } else {
        if (hasMembersAfterEntries) {
          append(";").newline().newline()
        } else {
          newline()
        }
      }
    }
  }

  // Properties
  for (p in d.properties) {
    renderProperty(p, ctx)
  }

  // Functions
  for (f in d.functions) {
    renderFunction(f, ctx)
  }

  // Companion object
  d.companion?.let { comp ->
    append("companion object {").newline()
    pushIndent()
    for (p in comp.properties) renderProperty(p, ctx)
    for (f in comp.functions) renderFunction(f, ctx)
    popIndent()
    append("}").newline()
  }

  popIndent()
  append("}").newline()
}

private fun IndentWriter.renderFunction(d: KtFunction, ctx: ImportContext) {
  renderKDoc(d.kdoc)
  renderAnnotations(d.annotations, ctx)
  renderModifiers(d.modifiers)
  append("fun ")
  if (d.typeParams.isNotEmpty()) {
    renderTypeParams(d.typeParams, ctx)
    append(" ")
  }
  append(escapeIfNeeded(d.name))
  append("(")
  var first = true
  for (p in d.params) {
    if (!first) append(", ") else first = false
    renderAnnotations(p.annotations, ctx, inline = true)
    if (p.annotations.isNotEmpty()) append(" ")
    append(escapeIfNeeded(p.name)).append(": ")
    renderType(p.type, ctx)
    p.default?.let { append(" = ").append(it.text) }
  }
  append(")")
  d.returnType?.let {
    append(": ")
    renderType(it, ctx)
  }

  if (d.body == null) {
    newline()
  } else {
    append(" {").newline()
    pushIndent()
    append(d.body.text).newline()
    popIndent()
    append("}").newline()
  }
}

private fun IndentWriter.renderProperty(d: KtProperty, ctx: ImportContext) {
  renderKDoc(d.kdoc)
  renderAnnotations(d.annotations, ctx)
  renderModifiers(d.modifiers)
  append(if (d.mutable) "var " else "val ")
  append(escapeIfNeeded(d.name)).append(": ")
  renderType(d.type, ctx)
  d.initializer?.let { append(" = ").append(it.text) }
  newline()
}
