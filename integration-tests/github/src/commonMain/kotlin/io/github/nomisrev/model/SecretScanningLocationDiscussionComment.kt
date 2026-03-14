package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName

@Serializable
@JvmInline
value class SecretScanningLocationDiscussionComment(@SerialName("discussion_comment_url") val discussionCommentUrl: String)
