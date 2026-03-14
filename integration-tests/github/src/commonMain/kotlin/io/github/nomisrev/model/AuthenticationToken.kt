package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class AuthenticationToken(
    val token: String,
    @SerialName("expires_at") val expiresAt: LocalDateTime,
    val permissions: JsonElement? = null,
    val repositories: List<Repository>? = null,
    @SerialName("single_file") val singleFile: String? = null,
    @SerialName("repository_selection") val repositorySelection: RepositorySelection? = null,
) {
    @Serializable
    enum class RepositorySelection {
        @SerialName("all") All, @SerialName("selected") Selected;
    }
}
