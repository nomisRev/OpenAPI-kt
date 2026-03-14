package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class InteractionGroup {
    @SerialName("existing_users")
    ExistingUsers,
    @SerialName("contributors_only")
    ContributorsOnly,
    @SerialName("collaborators_only")
    CollaboratorsOnly;
}
