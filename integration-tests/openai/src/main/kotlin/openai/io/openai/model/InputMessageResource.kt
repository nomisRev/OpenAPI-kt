package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A message input to the model with a role indicating instruction following
 * hierarchy. Instructions given with the `developer` or `system` role take
 * precedence over instructions given with the `user` role.
 *
 */
@Serializable
public data class InputMessageResource(
  public val type: Type? = null,
  public val role: Role,
  public val status: Status? = null,
  public val content: InputMessageContentList,
  public val id: String? = null,
) {
  @Serializable
  public enum class Role(
    public val `value`: String,
  ) {
    @SerialName("user")
    User("user"),
    @SerialName("system")
    System("system"),
    @SerialName("developer")
    Developer("developer"),
    ;
  }

  @Serializable
  public enum class Status(
    public val `value`: String,
  ) {
    @SerialName("in_progress")
    InProgress("in_progress"),
    @SerialName("completed")
    Completed("completed"),
    @SerialName("incomplete")
    Incomplete("incomplete"),
    ;
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("message")
    Message("message"),
    ;
  }
}
