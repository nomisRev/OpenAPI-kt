import io.github.nomisrev.openapi.program
import io.github.nomisrev.openapi.test.test
import okio.FileSystem

public fun main() {
  FileSystem.SYSTEM.test(
    pathSpec = "openai.json"
  )
}