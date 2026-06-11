package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Confirmation payload returned after adding a user to a group.
 */
@Serializable
public data class GroupUserAssignment(
  public val `object`: Object,
  @SerialName("user_id")
  public val userId: String,
  @SerialName("group_id")
  public val groupId: String,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("group.user")
    GroupUser("group.user"),
    ;
  }
}
