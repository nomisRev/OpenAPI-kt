package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A collection of related issues and pull requests.
 */
@Serializable
public data class NullableMilestone(
  public val url: String,
  @SerialName("html_url")
  public val htmlUrl: String,
  @SerialName("labels_url")
  public val labelsUrl: String,
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val number: Long,
  @Required
  public val state: State = State.Open,
  public val title: String,
  public val description: String?,
  public val creator: NullableSimpleUser?,
  @SerialName("open_issues")
  public val openIssues: Long,
  @SerialName("closed_issues")
  public val closedIssues: Long,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("closed_at")
  public val closedAt: Instant?,
  @SerialName("due_on")
  public val dueOn: Instant?,
) {
  @Serializable
  public enum class State(
    public val `value`: String,
  ) {
    @SerialName("open")
    Open("open"),
    @SerialName("closed")
    Closed("closed"),
    ;
  }
}
