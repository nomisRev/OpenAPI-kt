package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CodeScanningVariantAnalysisRepository(
    val id: Long,
    val name: String,
    @SerialName("full_name") val fullName: String,
    val private: Boolean,
    @SerialName("stargazers_count") val stargazersCount: Long,
    @SerialName("updated_at") val updatedAt: LocalDateTime?,
)
