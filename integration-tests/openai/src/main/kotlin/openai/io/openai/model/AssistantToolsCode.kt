package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class AssistantToolsCode(
  public val type: Type,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("code_interpreter")
    CodeInterpreter("code_interpreter"),
    ;
  }
}
