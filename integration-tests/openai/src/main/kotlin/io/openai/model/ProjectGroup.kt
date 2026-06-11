package io.openai.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Details about a group's membership in a project.
 */
@Serializable
public data class ProjectGroup(
  public val `object`: Object,
  @SerialName("project_id")
  public val projectId: String,
  @SerialName("group_id")
  public val groupId: String,
  @SerialName("group_name")
  public val groupName: String,
  @SerialName("created_at")
  public val createdAt: Long,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("project.group")
    ProjectGroup("project.group"),
    ;
  }
}
