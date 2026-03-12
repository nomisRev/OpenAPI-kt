package io.github.nomisrev.render

import com.goncalossilva.resources.Resource
import de.infix.testBalloon.framework.core.TestSuite
import de.infix.testBalloon.framework.shared.TestRegistering
import io.github.nomisrev.openapi.KFile
import io.ktor.http.ContentDisposition.Companion.File

private val updateGoldens: Boolean = false
private fun goldenBasePath(): String = "src/commonTest/resources/kotlinTestData"

expect fun writeFile(pathString: String, content: String)

@TestRegistering
fun TestSuite.verifyKotlin(
    name: String,
    resourceFile: String,
    actual: () -> String,
) = test(name) {
    val resourcePath = "kotlinTestData/$resourceFile"
    val resource = Resource(resourcePath)
    if (!resource.exists()) {
        if (updateGoldens) {
            writeFile("${goldenBasePath()}/$resourceFile", actual())
            println("Created golden: ${goldenBasePath()}/$resourceFile")
            return@test
        }
        throw AssertionError("Missing test resource: $resourcePath")
    }

    val expected = resource.readText()
    val rendered = actual()
    if (expected != rendered) {
        if (updateGoldens) {
            writeFile("${goldenBasePath()}/$resourceFile", rendered)
            println("Updated golden: ${goldenBasePath()}/$resourceFile")
            return@test
        }
        throw AssertionError(expected.diff(rendered))
    }
}

@TestRegistering
fun TestSuite.verifyKotlinFile(
    name: String,
    resourceFile: String,
    actual: () -> KFile,
) = verifyKotlin(name, resourceFile) { actual().content }

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
