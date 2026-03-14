package io.github.nomisrev.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class AuthorAssociation {
    COLLABORATOR,
    CONTRIBUTOR,
    @SerialName("FIRST_TIMER")
    FIRSTTIMER,
    @SerialName("FIRST_TIME_CONTRIBUTOR")
    FIRSTTIMECONTRIBUTOR,
    MANNEQUIN,
    MEMBER,
    NONE,
    OWNER;
}
