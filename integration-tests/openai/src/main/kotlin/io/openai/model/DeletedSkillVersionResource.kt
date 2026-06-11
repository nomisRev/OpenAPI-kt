package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class DeletedSkillVersionResource(
  @Required
  public val `object`: Object = Object.SkillVersionDeleted,
  public val deleted: Boolean,
  public val id: String,
  public val version: String,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("skill.version.deleted")
    SkillVersionDeleted("skill.version.deleted"),
    ;
  }
}
