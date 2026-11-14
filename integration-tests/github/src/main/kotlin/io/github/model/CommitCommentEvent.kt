package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CommitCommentEvent(
  public val action: String,
  public val comment: Comment,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Comment(
    @SerialName("html_url")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val htmlUrl: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val url: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val id: Long? = null,
    @SerialName("node_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val nodeId: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val body: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val path: Optional<String> = Optional.Absent,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val position: Optional<Long> = Optional.Absent,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val line: Optional<Long> = Optional.Absent,
    @SerialName("commit_id")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val commitId: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val user: Optional<NullableSimpleUser> = Optional.Absent,
    @SerialName("created_at")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val createdAt: Instant? = null,
    @SerialName("updated_at")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val updatedAt: Instant? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val reactions: ReactionRollup? = null,
  )
}
