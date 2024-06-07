import io.github.nomisrev.openapi.generateModel
import okio.FileSystem

public fun main() {
  FileSystem.SYSTEM.generateModel("openai.json")
}
