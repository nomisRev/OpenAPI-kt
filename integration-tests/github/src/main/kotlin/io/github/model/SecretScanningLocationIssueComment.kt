package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an 'issue_comment' secret scanning location type. This location type shows that a secret was detected in a comment on an issue.
 */
@JvmInline
@Serializable
public value class SecretScanningLocationIssueComment(
  @SerialName("issue_comment_url")
  public val issueCommentUrl: String,
)
