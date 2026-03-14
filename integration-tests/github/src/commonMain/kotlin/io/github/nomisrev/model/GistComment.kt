package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class GistComment(
    val id: Long,
    @SerialName("node_id") val nodeId: String,
    val url: String,
    val body: String,
    val user: NullableSimpleUser?,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    @SerialName("author_association") val authorAssociation: AuthorAssociation,
)
