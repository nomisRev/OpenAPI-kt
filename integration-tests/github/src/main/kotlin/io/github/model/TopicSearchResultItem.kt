package io.github.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Topic Search Result Item
 */
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
  public val textMatches: SearchResultTextMatches? = null,
  public val related: List<Related>? = null,
  public val aliases: List<Aliases>? = null,
) {
  @JvmInline
  @Serializable
  public value class Aliases(
    @SerialName("topic_relation")
    public val topicRelation: TopicRelation? = null,
  ) {
    @Serializable
    public data class TopicRelation(
      public val id: Long? = null,
      public val name: String? = null,
      @SerialName("topic_id")
      public val topicId: Long? = null,
      @SerialName("relation_type")
      public val relationType: String? = null,
    )
  }

  @JvmInline
  @Serializable
  public value class Related(
    @SerialName("topic_relation")
    public val topicRelation: TopicRelation? = null,
  ) {
    @Serializable
    public data class TopicRelation(
      public val id: Long? = null,
      public val name: String? = null,
      @SerialName("topic_id")
      public val topicId: Long? = null,
      @SerialName("relation_type")
      public val relationType: String? = null,
    )
  }
}
