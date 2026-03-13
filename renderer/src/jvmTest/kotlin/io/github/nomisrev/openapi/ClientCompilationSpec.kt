package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.parser.OpenAPI
import io.github.nomisrev.openapi.render.attemptDeserialize
import io.github.nomisrev.openapi.routes.toApiModel
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.test.Test

class ClientCompilationSpec {

    private fun testCompilation(fileName: String) = runBlocking {
        val file = File("../test-specs/$fileName")
        val content = file.readText()
        val spec = if (fileName.endsWith(".json")) {
            OpenAPI.fromJson(content)
        } else {
            OpenAPI.fromYaml(content)
        }
        val apiModel = spec.toApiModel()
        val modelFiles = apiModel.generate()

        val root = apiModel.routes.sort(spec.info.title, spec.servers)
        val clientFiles = root.generateClient()

        val files = modelFiles + clientFiles + KFile(
            "AttemptDeserialize.kt",
            "io.github.nomisrev.model",
            attemptDeserialize
        )

        // Write files for debugging
        val debugDir = File("build/generated-debug-client/${fileName.substringBeforeLast(".")}")
        debugDir.deleteRecursively()
        debugDir.mkdirs()
        println("Writing ${files.size} files to $debugDir")
        files.forEach { f ->
            val target = File(debugDir, f.name)
            target.parentFile?.mkdirs()
            target.writeText(f.content)
        }

        assertCompiles(files)
    }

    @Test
    fun `petstore client compiles`() = testCompilation("petstore.json")

    @Test
    fun `petstore_more client compiles`() = testCompilation("petstore_more.json")

    @Test
    fun `basic client compiles`() = testCompilation("basic.json")

// TODO: Fix one by one
//    @Test
//    fun github() = testCompilation("github.json")

//    @Test
//    fun `youtrack client compiles`() = testCompilation("youtrack.json")
}
