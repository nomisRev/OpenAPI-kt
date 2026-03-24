package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Paginated list of roles available on an organization or project.
 */
@Serializable
public data class PublicRoleListResource(
  public val `object`: Object,
  public val `data`: List<Role>,
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
