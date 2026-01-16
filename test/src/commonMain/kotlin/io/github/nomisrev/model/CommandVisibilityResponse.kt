package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface CommandVisibilityResponse {
    val id: String?

    @SerialName("Default")
    @Serializable
    @JvmInline
    value class Default(override val id: String? = null) : CommandVisibilityResponse

    @SerialName("CommandUnlimitedVisibility")
    @Serializable
    @JvmInline
    value class CommandUnlimitedVisibility(override val id: String? = null) : CommandVisibilityResponse

    @SerialName("CommandLimitedVisibility")
    @Serializable
    data class CommandLimitedVisibility(
        override val id: String? = null,
        val permittedGroups: List<UserGroupResponse>? = null,
        val permittedUsers: List<UserResponse>? = null,
    ) : CommandVisibilityResponse
}
