package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Confirmation payload returned after removing a user from a group.
 */
@Serializable
public data class GroupUserDeletedResource(
  public val `object`: Object,
  public val deleted: Boolean,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("group.user.deleted")
    GroupUserDeleted("group.user.deleted"),
    ;
  }
}
