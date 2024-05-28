import io.github.nomisrev.openapi.test
import kotlinx.io.files.SystemFileSystem

public fun main() {
  SystemFileSystem.test(
    pathSpec = "openai.json"
  )
}

// description
public sealed interface ChatCompletionToolChoiceOption {
  public data class CaseNoneOrAuto(val enum: NoneOrAuto)
  public data class CaseChatCompletionNamedToolChoice(val value: CaseChatCompletionNamedToolChoice)
  public enum class NoneOrAuto {
    none, auto;
  }
}

public data object CaseChatCompletionNamedToolChoice