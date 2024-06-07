package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.generation.DefaultNamingStrategy
import io.github.nomisrev.openapi.generation.ModelPredef
import io.github.nomisrev.openapi.generation.template
import io.github.nomisrev.openapi.generation.toCode
import okio.FileSystem
import okio.Path.Companion.toPath

public fun FileSystem.generateModel(
  pathSpec: String,
  `package`: String = "io.github.nomisrev.openapi",
  modelPackage: String = "$`package`.models",
  generationPath: String =
    "../example/build/generated/openapi/src/commonMain/kotlin/${`package`.replace(".", "/")}"
) {
  fun file(name: String, imports: Set<String>, code: String) {
    write("$generationPath/models/$name.kt".toPath()) {
      writeUtf8("${"package $modelPackage"}\n")
      writeUtf8("\n")
      if (imports.isNotEmpty()) {
        writeUtf8("${imports.joinToString("\n") { "import $it" }}\n")
        writeUtf8("\n")
      }
      writeUtf8("$code\n")
    }
  }

  deleteRecursively(generationPath.toPath(), false)
  runCatching { createDirectories("$generationPath/models".toPath(), mustCreate = false) }
  val rawSpec = read(pathSpec.toPath()) { readUtf8() }
  val openAPI = OpenAPI.fromJson(rawSpec)
  file("predef", emptySet(), ModelPredef)
  openAPI.models().forEach { model ->
    val strategy = DefaultNamingStrategy
    val content = template { toCode(model, strategy) }
    val name = strategy.typeName(model)
    file(name, content.imports, content.code)
  }
}
