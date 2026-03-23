package io.github.model

import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The status of a deployment.
 */
@Serializable
public data class DeploymentStatus(
  public val url: String,
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val state: State,
  public val creator: NullableSimpleUser?,
  @Required
  public val description: String = "",
  public val environment: String? = null,
  @SerialName("target_url")
  @Required
  public val targetUrl: String = "",
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("deployment_url")
  public val deploymentUrl: String,
  @SerialName("repository_url")
  public val repositoryUrl: String,
  @SerialName("environment_url")
  public val environmentUrl: String? = null,
  @SerialName("log_url")
  public val logUrl: String? = null,
  @SerialName("performed_via_github_app")
  public val performedViaGithubApp: NullableIntegration? = null,
) {
  @Serializable
  public enum class State(
    public val `value`: String,
  ) {
    @SerialName("error")
    Error("error"),
    @SerialName("failure")
    Failure("failure"),
    @SerialName("inactive")
    Inactive("inactive"),
    @SerialName("pending")
    Pending("pending"),
    @SerialName("success")
    Success("success"),
    @SerialName("queued")
    Queued("queued"),
    @SerialName("in_progress")
    InProgress("in_progress"),
    ;
  }
}
