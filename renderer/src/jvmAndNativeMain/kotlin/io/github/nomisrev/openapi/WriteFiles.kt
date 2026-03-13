package io.github.nomisrev.openapi

import io.github.nomisrev.openapi.render.attemptDeserialize
import io.github.nomisrev.openapi.routes.ApiModel
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.writeString

fun ApiModel.generate(name: String, output: String) {
    val files = generate() + root(name).generateClient()
    with(SystemFileSystem) {
        for (file in files) {
            val path = Path(output + "/" + file.packageName.replace('.', '/'))
            createDirectories(path)
            sink(Path("$output/${file.name}")).buffered().use { it.writeString(file.content) }
        }
        sink(Path("$output/io/github/nomisrev/model/AttemptDeserialize.kt"))
            .buffered()
            .use { it.writeString(attemptDeserialize) }
    }
}
