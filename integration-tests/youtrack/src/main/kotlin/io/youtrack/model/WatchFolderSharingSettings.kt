package io.youtrack.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Stores users and groups that have access to a tag or a saved search.
 */
@Serializable
public data class WatchFolderSharingSettings(
  public val id: String? = null,
  public val permittedGroups: List<UserGroupRead>? = null,
  public val permittedUsers: List<UserRead>? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
