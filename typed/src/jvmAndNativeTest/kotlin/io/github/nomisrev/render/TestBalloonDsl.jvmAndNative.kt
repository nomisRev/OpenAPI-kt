package io.github.nomisrev.render

import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.writeString

actual fun writeFile(pathString: String, content: String) =
    with(SystemFileSystem) {
        val path = Path(pathString)
        if (exists(path)) sink(Path(path)).buffered().use { it.writeString(content) }
        else {
            val dirs = pathString.substringBeforeLast("/")
            createDirectories(Path(dirs))
            sink(Path(path)).buffered().use { it.writeString(content) }
        }
    }