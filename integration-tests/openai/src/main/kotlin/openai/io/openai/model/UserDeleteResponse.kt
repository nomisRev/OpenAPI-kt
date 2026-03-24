package io.openai.model

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class UserDeleteResponse(
  public val `object`: Object,
  public val id: String,
  public val deleted: Boolean,
) {
  @Serializable
  public enum class Object(
    public val `value`: String,
  ) {
    @SerialName("organization.user.deleted")
    OrganizationUserDeleted("organization.user.deleted"),
    ;
  }
}
