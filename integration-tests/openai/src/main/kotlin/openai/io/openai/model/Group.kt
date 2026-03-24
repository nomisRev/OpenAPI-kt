package io.openai.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Summary information about a group returned in role assignment responses.
 */
@Serializable
public data class Group(
  public val `object`: Object,
  public val id: String,
  public val name: String,
  @SerialName("created_at")
  public val createdAt: Long,
  @SerialName("scim_managed")
  public val scimManaged: Boolean,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("group")
    Group("group"),
    ;
  }
}
