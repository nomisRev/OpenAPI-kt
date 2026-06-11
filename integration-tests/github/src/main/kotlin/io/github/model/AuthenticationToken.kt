package io.github.model

import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Authentication Token
 */
@Serializable
public data class AuthenticationToken(
  public val token: String,
  @SerialName("expires_at")
  public val expiresAt: Instant,
  public val permissions: JsonElement? = null,
  public val repositories: List<Repository>? = null,
  @SerialName("single_file")
  public val singleFile: String? = null,
  @SerialName("repository_selection")
  public val repositorySelection: RepositorySelection? = null,
) {
  @Serializable
  public enum class RepositorySelection(
    public val `value`: String,
  ) {
    @SerialName("all")
    All("all"),
    @SerialName("selected")
    Selected("selected"),
    ;
  }
}
