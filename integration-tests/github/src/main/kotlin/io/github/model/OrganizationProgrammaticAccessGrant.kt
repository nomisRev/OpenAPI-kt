package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.Map
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Minimal representation of an organization programmatic access grant for enumerations
 */
@Serializable
public data class OrganizationProgrammaticAccessGrant(
  public val id: Long,
  public val owner: SimpleUser,
  @SerialName("repository_selection")
  public val repositorySelection: RepositorySelection,
  @SerialName("repositories_url")
  public val repositoriesUrl: String,
  public val permissions: Permissions,
  @SerialName("access_granted_at")
  public val accessGrantedAt: String,
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
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Permissions(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val organization: Map<String, String>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val repository: Map<String, String>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val other: Map<String, String>? = null,
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
