package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface UserResponse {
    val id: String?
    val login: String?
    val fullName: String?
    val email: String?
    val ringId: String?
    val guest: Boolean?
    val online: Boolean?
    val banned: Boolean?
    val tags: List<Tag>?
    val savedQueries: List<SavedQuery>?
    val avatarUrl: String?
    val profiles: UserProfilesResponse?

    @SerialName("Default")
    @Serializable
    data class Default(
        override val id: String? = null,
        override val login: String? = null,
        override val fullName: String? = null,
        override val email: String? = null,
        override val ringId: String? = null,
        override val guest: Boolean? = null,
        override val online: Boolean? = null,
        override val banned: Boolean? = null,
        override val tags: List<Tag>? = null,
        override val savedQueries: List<SavedQuery>? = null,
        override val avatarUrl: String? = null,
        override val profiles: UserProfilesResponse? = null,
    ) : UserResponse

    @SerialName("VcsUnresolvedUser")
    @Serializable
    data class VcsUnresolvedUser(
        override val id: String? = null,
        override val login: String? = null,
        override val fullName: String? = null,
        override val email: String? = null,
        override val ringId: String? = null,
        override val guest: Boolean? = null,
        override val online: Boolean? = null,
        override val banned: Boolean? = null,
        override val tags: List<Tag>? = null,
        override val savedQueries: List<SavedQuery>? = null,
        override val avatarUrl: String? = null,
        override val profiles: UserProfilesResponse? = null,
        val name: String? = null,
    ) : UserResponse

    @SerialName("Me")
    @Serializable
    data class Me(
        override val id: String? = null,
        override val login: String? = null,
        override val fullName: String? = null,
        override val email: String? = null,
        override val ringId: String? = null,
        override val guest: Boolean? = null,
        override val online: Boolean? = null,
        override val banned: Boolean? = null,
        override val tags: List<Tag>? = null,
        override val savedQueries: List<SavedQuery>? = null,
        override val avatarUrl: String? = null,
        override val profiles: UserProfilesResponse? = null,
    ) : UserResponse
}
