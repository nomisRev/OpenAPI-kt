package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Specifies a tool the model should use. Use to force the model to call a specific tool.
 */
@Serializable
public data class AssistantsNamedToolChoice(
  public val type: Type,
  public val function: Function? = null,
) {
  @JvmInline
  @Serializable
  public value class Function(
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
