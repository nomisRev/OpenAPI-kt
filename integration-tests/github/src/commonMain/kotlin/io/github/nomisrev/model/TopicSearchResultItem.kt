package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline

@Serializable
data class TopicSearchResultItem(
    val name: String,
    @SerialName("display_name") val displayName: String?,
    @SerialName("short_description") val shortDescription: String?,
    val description: String?,
    @SerialName("created_by") val createdBy: String?,
    val released: String?,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    val featured: Boolean,
    val curated: Boolean,
    val score: Double,
    @SerialName("repository_count") val repositoryCount: Long? = null,
    @SerialName("logo_url") val logoUrl: String? = null,
    @SerialName("text_matches") val textMatches: SearchResultTextMatches? = null,
    val related: List<Related>? = null,
    val aliases: List<Aliases>? = null,
) {
    @Serializable
    @JvmInline
    value class Related(@SerialName("topic_relation") val topicRelation: TopicRelation? = null) {
        @Serializable
        data class TopicRelation(
            val id: Long? = null,
            val name: String? = null,
            @SerialName("topic_id") val topicId: Long? = null,
            @SerialName("relation_type") val relationType: String? = null,
        )
    }

    @Serializable
    @JvmInline
    value class Aliases(@SerialName("topic_relation") val topicRelation: TopicRelation? = null) {
        @Serializable
        data class TopicRelation(
            val id: Long? = null,
            val name: String? = null,
            @SerialName("topic_id") val topicId: Long? = null,
            @SerialName("relation_type") val relationType: String? = null,
        )
    }
}
