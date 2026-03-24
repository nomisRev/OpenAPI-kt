package io.openai.model

import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SkillReferenceParam(
  @Required
  public val type: Type = Type.SkillReference,
  @SerialName("skill_id")
  public val skillId: String,
  public val version: String? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("skill_reference")
    SkillReference("skill_reference"),
    ;
  }
}
