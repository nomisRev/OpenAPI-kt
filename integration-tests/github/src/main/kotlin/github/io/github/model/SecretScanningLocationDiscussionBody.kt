package io.github.model

import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a 'discussion_body' secret scanning location type. This location type shows that a secret was detected in the body of a discussion.
 */
@JvmInline
@Serializable
public value class SecretScanningLocationDiscussionBody(
  @SerialName("discussion_body_url")
  public val discussionBodyUrl: String,
)
