package io.github.nomisrev.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class OrganizationActionsSecret(
    val name: String,
    @SerialName("created_at") val createdAt: LocalDateTime,
    @SerialName("updated_at") val updatedAt: LocalDateTime,
    val visibility: Visibility,
    @SerialName("selected_repositories_url") val selectedRepositoriesUrl: String? = null,
) {
    @Serializable
    enum class Visibility {
        @SerialName("all") All, @SerialName("private") Private, @SerialName("selected") Selected;
    }
}
