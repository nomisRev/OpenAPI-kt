package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Paginated list of roles assigned to a principal.
 */
@Serializable
public data class RoleListResource(
  public val `object`: Object,
  public val `data`: List<AssignedRoleDetails>,
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
