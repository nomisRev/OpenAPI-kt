package io.github.nomisrev.render

import com.squareup.kotlinpoet.FileSpec
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertFailsWith

class TestBalloonDslTest {
    @Test
    fun renderGeneratedFilesRejectsDuplicateGeneratedFileKeys() {
        val duplicateA = FileSpec.builder("io.github.nomisrev.render.test", "AttemptDeserialize")
            .build()
        val duplicateB = FileSpec.builder("io.github.nomisrev.render.test", "AttemptDeserialize")
            .build()

        val error = assertFailsWith<IllegalStateException> {
            renderGeneratedFiles(listOf(duplicateA, duplicateB))
        }

        assertContains(error.message.orEmpty(), "io.github.nomisrev.render.test.AttemptDeserialize.kt")
    }
}
