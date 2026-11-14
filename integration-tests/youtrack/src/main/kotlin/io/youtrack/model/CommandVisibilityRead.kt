package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
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

  @SerialName("CommandVisibility")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
  ) : CommandVisibilityRead

  @SerialName("CommandUnlimitedVisibility")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class CommandUnlimitedVisibility(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
  ) : CommandVisibilityRead

  @SerialName("CommandLimitedVisibility")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class CommandLimitedVisibility(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val permittedGroups: List<UserGroupRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val permittedUsers: List<UserRead>? = null,
  ) : CommandVisibilityRead
}
