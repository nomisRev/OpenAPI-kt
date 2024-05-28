import io.github.nomisrev.openapi.test
import kotlinx.io.files.SystemFileSystem

public fun main() {
  SystemFileSystem.test(
    pathSpec = "openai.json"
  )
}