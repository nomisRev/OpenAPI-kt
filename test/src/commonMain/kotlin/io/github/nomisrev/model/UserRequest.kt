package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface UserRequest {
    val profiles: UserProfilesRequest?

    @SerialName("Default")
    @Serializable
    @JvmInline
    value class Default(override val profiles: UserProfilesRequest? = null) : UserRequest

    @SerialName("VcsUnresolvedUser")
    @Serializable
    @JvmInline
    value class VcsUnresolvedUser(override val profiles: UserProfilesRequest? = null) : UserRequest

    @SerialName("Me")
    @Serializable
    @JvmInline
    value class Me(override val profiles: UserProfilesRequest? = null) : UserRequest
}
