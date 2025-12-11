package io.github.nomisrev

import de.infix.testBalloon.framework.core.TestSuite
import de.infix.testBalloon.framework.core.testSuite
import de.infix.testBalloon.framework.shared.TestRegistering
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.routes.toApiModel
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString

val integrationSpec by testSuite {
    yamlSpec("openai.yaml")
    jsonSpec("github.json")
    jsonSpec("youtrack.json")
}

@TestRegistering
fun TestSuite.spec(api: OpenAPI) = test("${api.info.title} ${api.info.version}") {
    val result = api.toApiModel()
}

@TestRegistering
fun TestSuite.yamlSpec(name: String) =
    spec(OpenAPI.fromYaml(readText(name)))

@TestRegistering
fun TestSuite.jsonSpec(name: String) =
    spec(OpenAPI.fromJson(readText(name)))

private fun readText(path: String) = with(SystemFileSystem) {
    val candidates = listOf(
        Path("test-specs/$path"), // when working directory is the repository root
        Path("../test-specs/$path"), // when working directory is the module directory
    )
    val path = requireNotNull(candidates.firstOrNull { exists(it) }) {
        "Could not find youtrack.json in repository root. Checked: ${candidates.joinToString()}"
    }
    source(path).buffered().readString()
}