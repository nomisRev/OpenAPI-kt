package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Stores users and groups that have access to a tag.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class TagSharingSettings(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val permittedGroups: List<UserGroupRead>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val permittedUsers: List<UserRead>? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
