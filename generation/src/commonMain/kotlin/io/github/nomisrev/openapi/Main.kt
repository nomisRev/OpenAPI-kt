package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.generation.DefaultNamingStrategy
import io.github.nomisrev.openapi.generation.ModelPredef
import io.github.nomisrev.openapi.generation.template
import io.github.nomisrev.openapi.generation.toCode
import kotlinx.io.buffered
import kotlinx.io.bytestring.decodeToString
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteString
import kotlinx.io.writeString

public fun main() {
    SystemFileSystem.test(
        pathSpec = "openai.json"
    )
}

public fun FileSystem.test(
    pathSpec: String,
    `package`: String = "io.github.nomisrev.openapi",
    modelPackage: String = "$`package`.models",
    generationPath: String =
        "build/generated/openapi/src/commonMain/kotlin/${`package`.replace(".", "/")}"
) {
    fun file(name: String, imports: Set<String>, code: String) {
        sink(Path("$generationPath/models/$name.kt")).use { s ->
            with(s.buffered()) {
                writeString("${"package $modelPackage"}\n")
                writeString("\n")
                if (imports.isNotEmpty()) {
                    writeString("${imports.joinToString("\n") { "import $it" }}\n")
                    writeString("\n")
                }
                writeString("$code\n")
            }
        }
    }


    deleteRecursively(Path(generationPath), false)
    createDirectories(Path("$generationPath/models"))
    val rawSpec = source(Path(pathSpec)).buffered().use {
        it.readByteString().decodeToString()
    }
    val openAPI = OpenAPI.fromJson(rawSpec)
    file("predef", emptySet(), ModelPredef)
    openAPI.models().forEach { model ->
        val strategy = DefaultNamingStrategy
        val content = template { toCode(model, strategy) }
        val name = strategy.typeName(model)
        if (name in setOf("MessageStreamEvent", "RunStepStreamEvent", "RunStreamEvent", "AssistantStreamEvent")) Unit
        else file(name, content.imports, content.code)
    }
}

private fun FileSystem.deleteRecursively(path: Path, mustExist: Boolean = true) {
    if (metadataOrNull(path)?.isDirectory == true) {
        list(path).forEach {
            deleteRecursively(it, false)
        }
    }
    delete(path, mustExist)
}