package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ProjectUserUpdateRequest(
  public val role: Role,
) {
  @Serializable
  public enum class Role(
    public val `value`: String,
  ) {
    @SerialName("owner")
    Owner("owner"),
    @SerialName("member")
    Member("member"),
    ;
  }
}
