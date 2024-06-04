import io.github.nomisrev.openapi.generateClient
import okio.FileSystem

public fun main() {
  FileSystem.SYSTEM
    .generateClient("openai.json")
}