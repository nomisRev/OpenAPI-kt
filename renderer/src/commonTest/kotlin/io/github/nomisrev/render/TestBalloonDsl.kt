package io.github.nomisrev.render

import com.goncalossilva.resources.Resource
import de.infix.testBalloon.framework.core.TestSuite
import de.infix.testBalloon.framework.shared.TestRegistering
import io.github.nomisrev.openapi.KFile
import kotlinx.io.files.Path

@TestRegistering
fun TestSuite.verifyKotlinFiles(
    name: String,
    resourceDirectory: String,
    actual: suspend () -> List<KFile>,
) = test(name) {
    val actualFiles = actual()
    val rendered = actualFiles.snapshot()
    val expected = actualFiles
        .map { file ->
            val resourcePath = "kotlinTestData/$resourceDirectory/${file.name}"
            val resource = Resource(resourcePath)
            if (!resource.exists()) throw AssertionError("Missing test resource: $resourcePath")
            file.copy(content = resource.readText())
        }.snapshot()

    if (expected != rendered) {
        throw AssertionError(expected.diff(rendered))
    }
}

private fun List<KFile>.snapshot(): String =
    sortedBy { it.name }.joinToString("\n\n") { file ->
        "// ${file.name}\n${file.content.trimEnd()}"
    }
