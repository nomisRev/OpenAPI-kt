package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface VisibilityRequest {


    @SerialName("Default")
    @Serializable
    data object Default : VisibilityRequest

    @SerialName("UnlimitedVisibility")
    @Serializable
    data object UnlimitedVisibility : VisibilityRequest

    @SerialName("LimitedVisibility")
    @Serializable
    data class LimitedVisibility(
        val permittedGroups: List<UserGroupRequest>? = null,
        val permittedUsers: List<UserRequest>? = null,
    ) : VisibilityRequest
}
