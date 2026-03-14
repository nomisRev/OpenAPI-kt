package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CombinedCommitStatus(
    val state: String,
    val statuses: List<SimpleCommitStatus>,
    val sha: String,
    @SerialName("total_count") val totalCount: Long,
    val repository: MinimalRepository,
    @SerialName("commit_url") val commitUrl: String,
    val url: String,
)
