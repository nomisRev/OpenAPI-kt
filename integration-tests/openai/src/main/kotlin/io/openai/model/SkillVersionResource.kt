package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SkillVersionResource(
  @Required
  public val `object`: Object = Object.SkillVersion,
  public val id: String,
  @SerialName("skill_id")
  public val skillId: String,
  public val version: String,
  @SerialName("created_at")
  public val createdAt: Long,
  public val name: String,
  public val description: String,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("skill.version")
    SkillVersion("skill.version"),
    ;
  }
}
