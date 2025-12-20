package io.github.nomisrev.io.github.nomisrev.openapi

import de.infix.testBalloon.framework.core.TestSuite
import de.infix.testBalloon.framework.core.testSuite
import de.infix.testBalloon.framework.shared.TestRegistering
import io.github.nomisrev.api
import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.routes.toApiModel
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString

// Naive integration test to check it parsers correctly and passes the transformation.
val integrationSpec by testSuite {
    spec("openai.yaml")
    spec("github.json")
    spec("youtrack.json")
}

@TestRegistering
fun TestSuite.spec(name: String) =
    test("${api.info.title} ${api.info.version}") {
        if (name.endsWith("yaml")) OpenAPI.fromYaml(readText(name)) else OpenAPI.fromJson(readText(name))
        val ignored = api.toApiModel()
    }

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