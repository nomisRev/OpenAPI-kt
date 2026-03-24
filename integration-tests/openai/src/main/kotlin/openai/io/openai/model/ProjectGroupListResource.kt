package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Paginated list of groups that have access to a project.
 */
@Serializable
public data class ProjectGroupListResource(
  public val `object`: Object,
  public val `data`: List<ProjectGroup>,
  @SerialName("has_more")
  public val hasMore: Boolean,
  public val next: String?,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("list")
    List("list"),
    ;
  }
}
