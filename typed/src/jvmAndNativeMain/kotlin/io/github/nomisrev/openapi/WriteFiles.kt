package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.NamingContext.Companion.path
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.render.attemptDeserialize
import io.github.nomisrev.openapi.routes.ApiModel
import io.github.nomisrev.openapi.routes.SchemaContext
import io.github.nomisrev.openapi.routes.toApiModel
import kotlinx.coroutines.runBlocking
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.io.writeString

fun ApiModel.generate(output: String) {
    val files = generate()
    with(SystemFileSystem) {
        for (file in files) {
            val path = Path(output + "/" + file.packageName.replace('.', '/'))
            createDirectories(path)
            sink(Path("$path/${file.name}")).buffered().use { it.writeString(file.content) }
        }
        sink(Path("$output/io/github/nomisrev/model/AttemptDeserialize.kt"))
            .buffered()
            .use { it.writeString(attemptDeserialize) }
    }
}

fun main() = runBlocking {
    val githubJson = readText("youtrack.json")
    val api = OpenAPI.fromJson(githubJson).toApiModel()
    val path = Path(path("/test", "../test"), "/src/commonMain/kotlin")
    api.generate(path.toString())
}

private fun readText(path: String) = with(SystemFileSystem) {
    source(path("test-specs/$path", "../test-specs/$path")).buffered().readString()
}

private fun path(vararg path: String) = with(SystemFileSystem) {
    val candidates = path.map { Path(it) }
    requireNotNull(candidates.firstOrNull { exists(it) }) {
        "Could not find youtrack.json in repository root. Checked: ${candidates.joinToString()}"
    }
}