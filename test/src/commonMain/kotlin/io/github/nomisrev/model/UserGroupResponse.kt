package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface UserGroupResponse {
    val id: String?
    val name: String?
    val ringId: String?
    val usersCount: Long?
    val icon: String?
    val allUsersGroup: Boolean?
    val teamForProject: Project?

    @SerialName("Default")
    @Serializable
    data class Default(
        override val id: String? = null,
        override val name: String? = null,
        override val ringId: String? = null,
        override val usersCount: Long? = null,
        override val icon: String? = null,
        override val allUsersGroup: Boolean? = null,
        override val teamForProject: Project? = null,
    ) : UserGroupResponse

    @SerialName("AllUsersGroup")
    @Serializable
    data class AllUsersGroup(
        override val id: String? = null,
        override val name: String? = null,
        override val ringId: String? = null,
        override val usersCount: Long? = null,
        override val icon: String? = null,
        override val allUsersGroup: Boolean? = null,
        override val teamForProject: Project? = null,
    ) : UserGroupResponse

    @SerialName("RegisteredUsersGroup")
    @Serializable
    data class RegisteredUsersGroup(
        override val id: String? = null,
        override val name: String? = null,
        override val ringId: String? = null,
        override val usersCount: Long? = null,
        override val icon: String? = null,
        override val allUsersGroup: Boolean? = null,
        override val teamForProject: Project? = null,
    ) : UserGroupResponse

    @SerialName("NestedGroup")
    @Serializable
    data class NestedGroup(
        override val id: String? = null,
        override val name: String? = null,
        override val ringId: String? = null,
        override val usersCount: Long? = null,
        override val icon: String? = null,
        override val allUsersGroup: Boolean? = null,
        override val teamForProject: Project? = null,
    ) : UserGroupResponse

    @SerialName("ProjectTeam")
    @Serializable
    data class ProjectTeam(
        override val id: String? = null,
        override val name: String? = null,
        override val ringId: String? = null,
        override val usersCount: Long? = null,
        override val icon: String? = null,
        override val allUsersGroup: Boolean? = null,
        override val teamForProject: Project? = null,
        val project: Project? = null,
    ) : UserGroupResponse
}
