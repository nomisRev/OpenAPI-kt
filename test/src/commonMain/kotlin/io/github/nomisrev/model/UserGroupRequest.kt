package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface UserGroupRequest {
    val teamForProject: Project?

    @SerialName("Default")
    @Serializable
    @JvmInline
    value class Default(override val teamForProject: Project? = null) : UserGroupRequest

    @SerialName("AllUsersGroup")
    @Serializable
    @JvmInline
    value class AllUsersGroup(override val teamForProject: Project? = null) : UserGroupRequest

    @SerialName("RegisteredUsersGroup")
    @Serializable
    @JvmInline
    value class RegisteredUsersGroup(override val teamForProject: Project? = null) : UserGroupRequest

    @SerialName("NestedGroup")
    @Serializable
    @JvmInline
    value class NestedGroup(override val teamForProject: Project? = null) : UserGroupRequest

    @SerialName("ProjectTeam")
    @Serializable
    data class ProjectTeam(override val teamForProject: Project? = null, val project: Project? = null) : UserGroupRequest
}
