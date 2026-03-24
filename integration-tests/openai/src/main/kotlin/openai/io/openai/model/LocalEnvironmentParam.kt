package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class LocalEnvironmentParam(
  @Required
  public val type: Type = Type.Local,
  public val skills: List<LocalSkillParam>? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("local")
    Local("local"),
    ;
  }
}
