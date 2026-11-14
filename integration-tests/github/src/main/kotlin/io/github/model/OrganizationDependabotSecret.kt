package io.github.model

import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Secrets for GitHub Dependabot for an organization.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class OrganizationDependabotSecret(
  public val name: String,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  public val visibility: Visibility,
  @SerialName("selected_repositories_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val selectedRepositoriesUrl: String? = null,
) {
  @Serializable
  public enum class Visibility(
    public val `value`: String,
  ) {
    @SerialName("all")
    All("all"),
    @SerialName("private")
    Private("private"),
    @SerialName("selected")
    Selected("selected"),
    ;
  }
}
