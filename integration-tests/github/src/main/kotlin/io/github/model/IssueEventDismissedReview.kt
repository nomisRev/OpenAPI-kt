package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class IssueEventDismissedReview(
  public val state: String,
  @SerialName("review_id")
  public val reviewId: Long,
  @SerialName("dismissal_message")
  public val dismissalMessage: String?,
  @SerialName("dismissal_commit_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val dismissalCommitId: Optional<String> = Optional.Absent,
)
