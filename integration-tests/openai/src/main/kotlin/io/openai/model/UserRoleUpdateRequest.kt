package io.openai.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class UserRoleUpdateRequest(
  public val role: Role,
) {
  @Serializable
  public enum class Role(
    public val `value`: String,
  ) {
    @SerialName("owner")
    Owner("owner"),
    @SerialName("reader")
    Reader("reader"),
    ;
  }
}
