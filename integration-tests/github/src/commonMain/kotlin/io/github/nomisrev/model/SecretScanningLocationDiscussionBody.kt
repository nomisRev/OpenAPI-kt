package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class SecretScanningLocationDiscussionBody(@SerialName("discussion_body_url") val discussionBodyUrl: String)
