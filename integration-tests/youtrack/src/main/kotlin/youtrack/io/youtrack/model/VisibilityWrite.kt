package io.youtrack.model

import kotlin.OptIn
import kotlin.collections.List
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Represents visibility settings of an entity, for example, an issue or a comment.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface VisibilityWrite {
  @SerialName("Default")
  @Serializable
  public data object Default : VisibilityWrite

  @SerialName("UnlimitedVisibility")
  @Serializable
  public class UnlimitedVisibility() : VisibilityWrite

  @SerialName("LimitedVisibility")
  @Serializable
  public data class LimitedVisibility(
    public val permittedGroups: List<UserGroupWrite>? = null,
    public val permittedUsers: List<UserWrite>? = null,
  ) : VisibilityWrite
}
