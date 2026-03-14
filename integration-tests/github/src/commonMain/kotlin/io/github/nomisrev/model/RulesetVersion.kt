package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class RulesetVersion(
    @SerialName("version_id") val versionId: Long,
    val actor: Actor,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
) {
    @Serializable
    data class Actor(val id: Long? = null, val type: String? = null)
}
