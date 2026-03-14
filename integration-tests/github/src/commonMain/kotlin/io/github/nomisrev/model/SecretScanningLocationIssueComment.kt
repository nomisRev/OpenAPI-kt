package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class SecretScanningLocationIssueComment(@SerialName("issue_comment_url") val issueCommentUrl: String)
