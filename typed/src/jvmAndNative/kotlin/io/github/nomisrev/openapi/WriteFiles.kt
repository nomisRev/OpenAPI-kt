package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.NamingContext.Companion.path
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.routes.ApiModel
import io.github.nomisrev.openapi.routes.toApiModel
import kotlinx.coroutines.runBlocking
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.io.writeString

fun ApiModel.generate(output: String) {
    val files = generate()
    for (file in files) {
        val path = Path(output + "/" + file.packageName.replace('.', '/'))
        with(SystemFileSystem) {
            createDirectories(path)
            sink(Path("$path/${file.name}")).buffered().use { it.writeString(file.content) }
        }
    }
}

fun main() = runBlocking {
    val githubJson = readText("github.json")
    val api = OpenAPI.fromJson(githubJson).toApiModel()
    val path = Path(path("/test", "../test"), "/src/commonMain")
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