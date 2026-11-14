package io.github.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.time.Instant
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Topic Search Result Item
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class TopicSearchResultItem(
  public val name: String,
  @SerialName("display_name")
  public val displayName: String?,
  @SerialName("short_description")
  public val shortDescription: String?,
  public val description: String?,
  @SerialName("created_by")
  public val createdBy: String?,
  public val released: String?,
  @SerialName("created_at")
  public val createdAt: Instant,
  @SerialName("updated_at")
  public val updatedAt: Instant,
  public val featured: Boolean,
  public val curated: Boolean,
  public val score: Double,
  @SerialName("repository_count")
  public val repositoryCount: Long? = null,
  @SerialName("logo_url")
  public val logoUrl: String? = null,
  @SerialName("text_matches")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val textMatches: SearchResultTextMatches? = null,
  public val related: List<Related>? = null,
  public val aliases: List<Aliases>? = null,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Aliases(
    @SerialName("topic_relation")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val topicRelation: TopicRelation? = null,
  ) {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class TopicRelation(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val id: Long? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val name: String? = null,
      @SerialName("topic_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val topicId: Long? = null,
      @SerialName("relation_type")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val relationType: String? = null,
    )
  }

  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Related(
    @SerialName("topic_relation")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val topicRelation: TopicRelation? = null,
  ) {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class TopicRelation(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val id: Long? = null,
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val name: String? = null,
      @SerialName("topic_id")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val topicId: Long? = null,
      @SerialName("relation_type")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val relationType: String? = null,
    )
  }
}
