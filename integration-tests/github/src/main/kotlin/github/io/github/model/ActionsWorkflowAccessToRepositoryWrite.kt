package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class ActionsWorkflowAccessToRepositoryWrite(
  @SerialName("access_level")
  public val accessLevel: AccessLevel,
) {
  @Serializable
  public enum class AccessLevel(
    public val `value`: String,
  ) {
    @SerialName("none")
    None("none"),
    @SerialName("user")
    User("user"),
    @SerialName("organization")
    Organization("organization"),
    ;
  }
}
