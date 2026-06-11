package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an 'issue_body' secret scanning location type. This location type shows that a secret was detected in the body of an issue.
 */
@JvmInline
@Serializable
public value class SecretScanningLocationIssueBody(
  @SerialName("issue_body_url")
  public val issueBodyUrl: String,
)
