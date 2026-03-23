package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A GitHub organization.
 */
@Serializable
public data class SimpleClassroomOrganization(
  public val id: Long,
  public val login: String,
  @SerialName("node_id")
  public val nodeId: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  public val name: String?,
  @SerialName("avatar_url")
  public val avatarUrl: String,
)
