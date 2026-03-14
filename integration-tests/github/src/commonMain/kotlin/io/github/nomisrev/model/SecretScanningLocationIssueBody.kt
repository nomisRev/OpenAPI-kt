package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class SecretScanningLocationIssueBody(@SerialName("issue_body_url") val issueBodyUrl: String)
