package io.github.nomisrev.render

import com.goncalossilva.resources.Resource
import de.infix.testBalloon.framework.core.TestSuite
import de.infix.testBalloon.framework.shared.TestRegistering
import io.github.nomisrev.openapi.KFile
import io.ktor.http.ContentDisposition.Companion.File

private val updateGoldens: Boolean = true
private fun goldenBasePath(): String = "src/commonTest/resources/kotlinTestData"

expect fun writeFile(pathString: String, content: String)

@TestRegistering
fun TestSuite.verifyKotlinFiles(
    name: String,
    resourceDirectory: String,
    actual: suspend () -> List<KFile>,
) = test(name) {
    val actualFiles = actual()
    val rendered = actualFiles.snapshot()

    // Check for missing golden files
    val missingFiles = actualFiles.filter { file ->
        val resourcePath = "kotlinTestData/$resourceDirectory/${file.name}"
        !Resource(resourcePath).exists()
    }

    if (missingFiles.isNotEmpty() && updateGoldens) {
        // Create all golden files
        actualFiles.forEach { file ->
            writeFile("${goldenBasePath()}/$resourceDirectory/${file.name}", file.content)
            println("Created golden: ${goldenBasePath()}/$resourceDirectory/${file.name}")
        }
        return@test
    }

    val expected = actualFiles
        .map { file ->
            val resourcePath = "kotlinTestData/$resourceDirectory/${file.name}"
            val resource = Resource(resourcePath)
            if (!resource.exists()) {
                throw AssertionError("Missing test resource: $resourcePath")
            }
            file.copy(content = resource.readText())
        }
        .snapshot()

    if (expected != rendered) {
        if (updateGoldens) {
            actualFiles.forEach { file ->
                writeFile("${goldenBasePath()}/$resourceDirectory/${file.name}", file.content)
                println("Updated golden: ${goldenBasePath()}/$resourceDirectory/${file.name}")
            }
            return@test
        }
        throw AssertionError(expected.diff(rendered))
    }
}

private fun List<KFile>.snapshot(): String =
    sortedBy { it.name }.joinToString("\n\n") { file ->
        "// ${file.name}\n${file.content.trimEnd()}"
    }
