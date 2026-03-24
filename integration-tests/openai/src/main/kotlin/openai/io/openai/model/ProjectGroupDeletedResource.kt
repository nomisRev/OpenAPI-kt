package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Confirmation payload returned after removing a group from a project.
 */
@Serializable
public data class ProjectGroupDeletedResource(
  public val `object`: Object,
  public val deleted: Boolean,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("project.group.deleted")
    ProjectGroupDeleted("project.group.deleted"),
    ;
  }
}
