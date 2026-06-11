package io.youtrack.model

import kotlin.OptIn
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
public sealed interface CommandVisibilityWrite {
  @SerialName("CommandVisibility")
  @Serializable
  public class Default() : CommandVisibilityWrite

  @SerialName("CommandUnlimitedVisibility")
  @Serializable
  public class CommandUnlimitedVisibility() : CommandVisibilityWrite

  @SerialName("CommandLimitedVisibility")
  @Serializable
  public class CommandLimitedVisibility() : CommandVisibilityWrite
}
