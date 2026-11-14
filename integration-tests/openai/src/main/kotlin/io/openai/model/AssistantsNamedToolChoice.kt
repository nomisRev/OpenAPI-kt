package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Specifies a tool the model should use. Use to force the model to call a specific tool.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class AssistantsNamedToolChoice(
  public val type: Type,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val function: Function? = null,
) {
  @Serializable
  public data class Function(
    public val name: String,
  )

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("function")
    Function("function"),
    @SerialName("code_interpreter")
    CodeInterpreter("code_interpreter"),
    @SerialName("file_search")
    FileSearch("file_search"),
    ;
  }
}
