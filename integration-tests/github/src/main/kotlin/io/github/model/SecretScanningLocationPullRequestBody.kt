package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a 'pull_request_body' secret scanning location type. This location type shows that a secret was detected in the body of a pull request.
 */
@Serializable
public data class SecretScanningLocationPullRequestBody(
  @SerialName("pull_request_body_url")
  public val pullRequestBodyUrl: String,
)
