package io.github.model

import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Organization variable for GitHub Actions.
 */
@Serializable
public data class OrganizationActionsVariable(
  public val name: String,
  public val `value`: String,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  public val visibility: Visibility,
  @SerialName("selected_repositories_url")
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
