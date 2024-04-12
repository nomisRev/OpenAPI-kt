import io.github.nomisrev.openapi.test
import okio.FileSystem

public fun main() {
  FileSystem.SYSTEM.test(
    pathSpec = "openai.json"
  )
}