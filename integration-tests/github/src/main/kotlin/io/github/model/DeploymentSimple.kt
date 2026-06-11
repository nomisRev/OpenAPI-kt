package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A deployment created as the result of an Actions check run from a workflow that references an environment
 */
@Serializable
public data class DeploymentSimple(
  public val url: String,
  public val id: Long,
  @SerialName("node_id")
  public val nodeId: String,
  public val task: String,
  @SerialName("original_environment")
  public val originalEnvironment: String? = null,
  public val environment: String,
  public val description: String?,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  @SerialName("statuses_url")
  public val statusesUrl: String,
  @SerialName("repository_url")
  public val repositoryUrl: String,
  @SerialName("transient_environment")
  public val transientEnvironment: Boolean? = null,
  @SerialName("production_environment")
  public val productionEnvironment: Boolean? = null,
  @SerialName("performed_via_github_app")
  public val performedViaGithubApp: NullableIntegration? = null,
)
