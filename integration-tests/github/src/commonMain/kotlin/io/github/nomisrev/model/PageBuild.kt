package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlin.jvm.JvmInline

@Serializable
data class PageBuild(
    val url: String,
    val status: String,
    val error: Error,
    val pusher: NullableSimpleUser?,
    val commit: String,
    val duration: Long,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
) {
    @Serializable
    @JvmInline
    value class Error(val message: String?)
}
