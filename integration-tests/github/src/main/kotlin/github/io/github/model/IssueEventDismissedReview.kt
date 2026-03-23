package io.github.model

import kotlin.Long
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class IssueEventDismissedReview(
  public val state: String,
  @SerialName("review_id")
  public val reviewId: Long,
  @SerialName("dismissal_message")
  public val dismissalMessage: String?,
  @SerialName("dismissal_commit_id")
  public val dismissalCommitId: String? = null,
)
