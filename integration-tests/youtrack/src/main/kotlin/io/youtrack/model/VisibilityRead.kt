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
 * Represents visibility settings of an entity, for example, an issue or a comment.
 */
@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface VisibilityRead {
  public val id: String?

  @SerialName("Visibility")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
  ) : VisibilityRead

  @SerialName("UnlimitedVisibility")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class UnlimitedVisibility(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
  ) : VisibilityRead

  @SerialName("LimitedVisibility")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class LimitedVisibility(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val permittedGroups: List<UserGroupRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val permittedUsers: List<UserRead>? = null,
  ) : VisibilityRead
}
