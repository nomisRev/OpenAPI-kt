package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.generation.DefaultNamingStrategy
import io.github.nomisrev.openapi.generation.ModelPredef
import io.github.nomisrev.openapi.generation.template
import io.github.nomisrev.openapi.generation.toPropertyCode
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

public fun FileSystem.generateClient(
  pathSpec: String,
  `package`: String = "io.github.nomisrev.openapi",
  modelPackage: String = "$`package`.models",
  generationPath: String =
    "../example/build/generated/openapi/src/commonMain/kotlin/${`package`.replace(".", "/")}"
) {
  fun file(name: String, imports: Set<String>, code: String) {
    write(Path("$generationPath/models/$name.kt")) {
      writeUtf8("${"package $modelPackage"}\n")
      writeUtf8("\n")
      if (imports.isNotEmpty()) {
        writeUtf8("${imports.joinToString("\n") { "import $it" }}\n")
        writeUtf8("\n")
      }
      writeUtf8("$code\n")
    }
  }

  deleteRecursively(Path(generationPath), false)
  runCatching { createDirectories(Path("$generationPath/models"), mustCreate = false) }
  val rawSpec = read(Path(pathSpec)) { readUtf8() }
  val openAPI = OpenAPI.fromJson(rawSpec)
  file("predef", emptySet(), ModelPredef)
  openAPI.models().forEach { model ->
    val strategy = DefaultNamingStrategy
    val content = template { toPropertyCode(model, strategy) }
    val name = strategy.typeName(model)
//    if (name in setOf("MessageStreamEvent", "RunStepStreamEvent", "RunStreamEvent", "AssistantStreamEvent")) Unit
//    else
    file(name, content.imports, content.code)
  }

  val routes = openAPI
    .routes()
    .groupBy { route ->
      route.path.takeWhile { it != '{' }.dropLastWhile { it == '/' }
    }.mapValues { (path, routes) -> extracted(path, routes) }
    .forEach { (path, structure) ->
      val strategy = DefaultNamingStrategy
    }
}

private fun extracted(path: String, routes: List<Route>): Structure {
  val split = regex.split(path, limit = 2)
  return if (split.size == 2) Structure.Nested(split[1], extracted(split[1], routes))
  else Structure.Value(routes)
}

private val regex = "^(.+?)/".toRegex()

/**
 * Structure that helps us define the structure of the routes in OpenAPI.
 * We want the generated API to look like the URLs, and their OpenAPI Specification.
 *
 * So we generate the API according to the structure of the OpenAPI Specification.
 * A top-level interface `info.title`, or custom name.
 *
 * And within the interface all operations are available as their URL, with operationId.
 * An example for `OpenAI` `/chat/completion` with operationId `createChatCompletion`.
 *
 * interface OpenAI {
 *   val chat: Chat
 * }
 *
 * interface Chat {
 *   val completions: Completions
 * }
 *
 * interface Completions {
 *   fun createChatCompletion(): CreateChatCompletionResponse
 * }
 *
 * This requires us to split paths in a sane way,
 * such that we can follow the structure of the specification.
 */
sealed interface Structure {
  data class Value(val route: List<Route>) : Structure
  data class Nested(
    val name: String,
    val route: Structure
  ) : Structure
}

private fun Path(path: String): Path =
  path.toPath()