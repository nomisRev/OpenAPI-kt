package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Minimal representation of an organization programmatic access grant request for enumerations
 */
@Serializable
public data class OrganizationProgrammaticAccessGrantRequest(
  public val id: Long,
  public val reason: String?,
  public val owner: SimpleUser,
  @SerialName("repository_selection")
  public val repositorySelection: RepositorySelection,
  @SerialName("repositories_url")
  public val repositoriesUrl: String,
  public val permissions: Permissions,
  @SerialName("created_at")
  public val createdAt: String,
  @SerialName("token_id")
  public val tokenId: Long,
  @SerialName("token_name")
  public val tokenName: String,
  @SerialName("token_expired")
  public val tokenExpired: Boolean,
  @SerialName("token_expires_at")
  public val tokenExpiresAt: String?,
  @SerialName("token_last_used_at")
  public val tokenLastUsedAt: String?,
) {
  /**
   * Permissions requested, categorized by type of permission.
   */
  @Serializable
  public data class Permissions(
    public val organization: List<String>? = null,
    public val repository: List<String>? = null,
    public val other: List<String>? = null,
  )

  @Serializable
  public enum class RepositorySelection(
    public val `value`: String,
  ) {
    @SerialName("none")
    None("none"),
    @SerialName("all")
    All("all"),
    @SerialName("subset")
    Subset("subset"),
    ;
  }
}
