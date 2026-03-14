package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class RulesetVersionWithState(
    @SerialName("version_id") val versionId: Long,
    val actor: Actor,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    val state: JsonElement? = null,
) {
    @Serializable
    data class Actor(val id: Long? = null, val type: String? = null)
}
