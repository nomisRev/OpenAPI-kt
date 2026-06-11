package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an 'issue_title' secret scanning location type. This location type shows that a secret was detected in the title of an issue.
 */
@JvmInline
@Serializable
public value class SecretScanningLocationIssueTitle(
  @SerialName("issue_title_url")
  public val issueTitleUrl: String,
)
