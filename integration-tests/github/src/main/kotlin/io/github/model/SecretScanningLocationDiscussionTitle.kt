package io.github.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a 'discussion_title' secret scanning location type. This location type shows that a secret was detected in the title of a discussion.
 */
@Serializable
public data class SecretScanningLocationDiscussionTitle(
  @SerialName("discussion_title_url")
  public val discussionTitleUrl: String,
)
