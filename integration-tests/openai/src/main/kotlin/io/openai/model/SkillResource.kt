package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class SkillResource(
  public val id: String,
  @Required
  public val `object`: Object = Object.Skill,
  public val name: String,
  public val description: String,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("default_version")
  public val defaultVersion: String,
  @SerialName("latest_version")
  public val latestVersion: String,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("skill")
    Skill("skill"),
    ;
  }
}
