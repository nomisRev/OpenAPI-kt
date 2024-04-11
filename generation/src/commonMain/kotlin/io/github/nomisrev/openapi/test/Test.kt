package io.github.nomisrev.openapi.test

import io.github.nomisrev.openapi.OpenAPI
import okio.BufferedSink
import okio.BufferedSource
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer
import okio.use

private fun BufferedSink.writeUtf8Line(line: String) {
  writeUtf8("$line\n")
}

private fun BufferedSink.writeUtf8Line() {
  writeUtf8("\n")
}

public fun FileSystem.test(
  pathSpec: String,
  `package`: String = "io.github.nomisrev.openapi",
  modelPackage: String = "$`package`.models",
  generationPath: String =
    "build/generated/openapi/src/commonMain/kotlin/${`package`.replace(".", "/")}"
) {
  fun file(name: String, imports: Set<String>, code: String) {
    write("$generationPath/models/$name.kt".toPath()) {
      writeUtf8Line("package $modelPackage")
      writeUtf8Line()
//      if (imports.isNotEmpty()) {
//        writeUtf8Line(imports.joinToString("\n") { "import ${it.`package`}.${it.typeName}" })
//        writeUtf8Line()
//      }
      writeUtf8Line(code)
    }
  }

  deleteRecursively(generationPath.toPath())
  createDirectories("$generationPath/models".toPath())
  val rawSpec = source(pathSpec.toPath()).buffer().use(BufferedSource::readUtf8)
  val openAPI = OpenAPI.fromJson(rawSpec)
  file("predef", emptySet(), predef)
  openAPI.models().forEach { model ->
    val name = model.typeName()
    file(
      model.typeName(),
      setOf(),
      template { toCode(model) }
    )
  }
}