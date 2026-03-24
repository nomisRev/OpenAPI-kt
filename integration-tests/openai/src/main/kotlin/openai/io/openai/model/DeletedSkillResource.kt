package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class DeletedSkillResource(
  @Required
  public val `object`: Object = Object.SkillDeleted,
  public val deleted: Boolean,
  public val id: String,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("skill.deleted")
    SkillDeleted("skill.deleted"),
    ;
  }
}
