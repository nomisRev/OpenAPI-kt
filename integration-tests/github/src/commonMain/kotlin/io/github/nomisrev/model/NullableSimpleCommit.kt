package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class NullableSimpleCommit(
    val id: String,
    @SerialName("tree_id") val treeId: String,
    val message: String,
    val timestamp: LocalDateTime,
    val author: Author?,
    val committer: Committer?,
) {
    @Serializable
    data class Author(val name: String, val email: String)

    @Serializable
    data class Committer(val name: String, val email: String)
}
