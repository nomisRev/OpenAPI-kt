package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Visibility settings of the comment that is added along with the command.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface CommandVisibilityRead {
  public val id: String?

  @JvmInline
  @SerialName("CommandVisibility")
  @Serializable
  public value class Default(
    override val id: String? = null,
  ) : CommandVisibilityRead

  @JvmInline
  @SerialName("CommandUnlimitedVisibility")
  @Serializable
  public value class CommandUnlimitedVisibility(
    override val id: String? = null,
  ) : CommandVisibilityRead

  @SerialName("CommandLimitedVisibility")
  @Serializable
  public data class CommandLimitedVisibility(
    override val id: String? = null,
    public val permittedGroups: List<UserGroupRead>? = null,
    public val permittedUsers: List<UserRead>? = null,
  ) : CommandVisibilityRead
}
