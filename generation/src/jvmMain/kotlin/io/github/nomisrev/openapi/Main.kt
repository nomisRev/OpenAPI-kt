package io.github.nomisrev.openapi

import okio.FileSystem

public fun main() {
    FileSystem.SYSTEM
        .generateClient("openai.json")
}
