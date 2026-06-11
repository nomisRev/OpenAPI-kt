package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a 'pull_request_title' secret scanning location type. This location type shows that a secret was detected in the title of a pull request.
 */
@JvmInline
@Serializable
public value class SecretScanningLocationPullRequestTitle(
  @SerialName("pull_request_title_url")
  public val pullRequestTitleUrl: String,
)
