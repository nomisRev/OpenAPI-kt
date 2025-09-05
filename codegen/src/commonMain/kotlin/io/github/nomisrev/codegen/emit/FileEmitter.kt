package io.github.nomisrev.codegen.emit

import io.github.nomisrev.codegen.api.CodegenOptions
import io.github.nomisrev.codegen.api.GeneratedFile
import io.github.nomisrev.codegen.format.IndentWriter
import io.github.nomisrev.codegen.ir.KtFile
import io.github.nomisrev.codegen.util.ImportResolver

fun emitFile(file: KtFile, options: CodegenOptions = CodegenOptions()): String {
  val ctx = ImportResolver.compute(file)
  val w = IndentWriter(indent = options.indent)

  // KDoc on file (optional)
  w.renderKDoc(file.kdoc)

  // package
  file.pkg?.let { pkg ->
    w.append("package ").append(pkg).newline()
    w.newline()
  }

  // imports: ImportResolver already provided the desired order
  val imports = ctx.imported
  if (imports.isNotEmpty()) {
    for (imp in imports) {
      w.append("import ").append(imp).newline()
    }
    w.newline()
  }

  // declarations
  var firstDecl = true
  for (decl in file.declarations) {
    if (!firstDecl) w.newline() else firstDecl = false
    w.renderDeclaration(decl, ctx)
  }

  return w.toString()
}

fun generate(file: KtFile, options: CodegenOptions = CodegenOptions()): GeneratedFile {
  val content = emitFile(file, options)
  val path =
    if (file.pkg.isNullOrEmpty()) file.name else file.pkg.replace('.', '/') + "/" + file.name
  return GeneratedFile(path, content)
}
