package io.github.nomisrev.render

import com.goncalossilva.resources.Resource
import de.infix.testBalloon.framework.core.TestSuite
import de.infix.testBalloon.framework.shared.TestRegistering
import io.github.nomisrev.openapi.KFile

@TestRegistering
fun TestSuite.verifyKotlin(
    name: String,
    resourceFile: String,
    actual: () -> String,
) = test(name) {
    val resourcePath = "kotlinTestData/$resourceFile"
    val resource = Resource(resourcePath)
    if (!resource.exists()) {
        throw AssertionError("Missing test resource: $resourcePath")
    }

    val expected = resource.readText()
    val rendered = actual()
    if (expected != rendered) {
        throw AssertionError(expected.diff(rendered))
    }
}

@TestRegistering
fun TestSuite.verifyKotlinFile(
    name: String,
    resourceFile: String,
    actual: () -> KFile,
) = verifyKotlin(name, resourceFile) { actual().content }
