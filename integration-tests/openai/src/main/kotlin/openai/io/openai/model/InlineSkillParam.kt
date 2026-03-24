package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class InlineSkillParam(
  @Required
  public val type: Type = Type.Inline,
  public val name: String,
  public val description: String,
  public val source: InlineSkillSourceParam,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("inline")
    Inline("inline"),
    ;
  }
}
