import io.github.nomisrev.openapi.program
import okio.FileSystem

public fun main() {
  FileSystem.SYSTEM.program(
    pathSpec = "openai.json"
  )
}