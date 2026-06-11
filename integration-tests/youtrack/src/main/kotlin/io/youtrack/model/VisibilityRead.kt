package io.youtrack.model

import kotlin.OptIn
import kotlin.String
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
public sealed interface VisibilityRead {
  public val id: String?

  @SerialName("Visibility")
  @Serializable
  public data class Default(
    override val id: String? = null,
  ) : VisibilityRead

  @SerialName("UnlimitedVisibility")
  @Serializable
  public data class UnlimitedVisibility(
    override val id: String? = null,
  ) : VisibilityRead

  @SerialName("LimitedVisibility")
  @Serializable
  public data class LimitedVisibility(
    override val id: String? = null,
    public val permittedGroups: List<UserGroupRead>? = null,
    public val permittedUsers: List<UserRead>? = null,
  ) : VisibilityRead
}
