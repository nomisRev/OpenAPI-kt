package io.github.nomisrev.model

import kotlinx.serialization.Serializable

@Serializable
data class TagSharingSettingsRequest(
    val permittedGroups: List<UserGroupRequest>? = null,
    val permittedUsers: List<UserRequest>? = null,
)
