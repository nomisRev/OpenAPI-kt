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

  val builder = LayerBuilder("OpenAPI", mutableListOf(), mutableListOf())
  openAPI.routes().forEach { route ->
    // For now we reduce paths like `/file/{file_id}/content` to `file`
    val normalised = route.path.takeWhile { it != '{' }.dropLastWhile { it == '/' }
    val parts = normalised.split("/").filter { it.isNotEmpty() }
    parts.fold(builder) { acc, part ->
      val existing = acc.nested.find { it.name == part }
      if (existing != null) {
        existing.route.add(route)
        existing
      } else {
        val new = LayerBuilder(part, mutableListOf(route), mutableListOf())
        acc.nested.add(new)
        new
      }
    }
  }
  val layer = builder.build()
}

/**
 * Structure that helps us define the structure of the routes in OpenAPI. We want the generated API
 * to look like the URLs, and their OpenAPI Specification.
 *
 * So we generate the API according to the structure of the OpenAPI Specification. A top-level
 * interface `info.title`, or custom name.
 *
 * And within the interface all operations are available as their URL, with operationId. An example
 * for `OpenAI` `/chat/completion` with operationId `createChatCompletion`.
 *
 * interface OpenAI { val chat: Chat }
 *
 * interface Chat { val completions: Completions }
 *
 * interface Completions { fun createChatCompletion(): CreateChatCompletionResponse }
 *
 * This requires us to split paths in a sane way, such that we can follow the structure of the
 * specification.
 */
data class Layer(val name: String, val route: List<Route>, val nested: List<Layer>?)

data class LayerBuilder(
  val name: String,
  val route: MutableList<Route>,
  val nested: MutableList<LayerBuilder>
) {
  fun build(): Layer = Layer(name, route, nested.map { it.build() })
}
