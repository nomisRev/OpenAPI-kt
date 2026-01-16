package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface VisibilityResponse {
    val id: String?

    @SerialName("Default")
    @Serializable
    @JvmInline
    value class Default(override val id: String? = null) : VisibilityResponse

    @SerialName("UnlimitedVisibility")
    @Serializable
    @JvmInline
    value class UnlimitedVisibility(override val id: String? = null) : VisibilityResponse

    @SerialName("LimitedVisibility")
    @Serializable
    data class LimitedVisibility(
        override val id: String? = null,
        val permittedGroups: List<UserGroupResponse>? = null,
        val permittedUsers: List<UserResponse>? = null,
    ) : VisibilityResponse
}
