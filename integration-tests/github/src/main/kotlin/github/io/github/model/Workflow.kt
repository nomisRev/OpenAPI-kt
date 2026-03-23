package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A GitHub Actions workflow
 */
@Serializable
public data class Workflow(
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val name: String,
  public val path: String,
  public val state: State,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  public val url: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("badge_url")
  public val badgeUrl: String,
  @SerialName("deleted_at")
  public val deletedAt: Instant? = null,
) {
  @Serializable
  public enum class State(
    public val `value`: String,
  ) {
    @SerialName("active")
    Active("active"),
    @SerialName("deleted")
    Deleted("deleted"),
    @SerialName("disabled_fork")
    DisabledFork("disabled_fork"),
    @SerialName("disabled_inactivity")
    DisabledInactivity("disabled_inactivity"),
    @SerialName("disabled_manually")
    DisabledManually("disabled_manually"),
    ;
  }
}
