package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A GitHub organization.
 */
@Serializable
public data class NullableOrganizationSimple(
  public val login: String,
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val url: String,
  @SerialName("repos_url")
  public val reposUrl: String,
  @SerialName("events_url")
  public val eventsUrl: String,
  @SerialName("hooks_url")
  public val hooksUrl: String,
  @SerialName("issues_url")
  public val issuesUrl: String,
  @SerialName("members_url")
  public val membersUrl: String,
  @SerialName("public_members_url")
  public val publicMembersUrl: String,
  @SerialName("avatar_url")
  public val avatarUrl: String,
  public val description: String?,
)
