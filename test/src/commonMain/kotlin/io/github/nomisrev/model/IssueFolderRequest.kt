package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface IssueFolderRequest {
    val name: String?

    @SerialName("Default")
    @Serializable
    @JvmInline
    value class Default(override val name: String? = null) : IssueFolderRequest

    @SerialName("WatchFolder")
    @Serializable
    data class WatchFolder(
        override val name: String? = null,
        val owner: UserRequest? = null,
        val visibleFor: UserGroupRequest? = null,
        val updateableBy: UserGroupRequest? = null,
        val readSharingSettings: WatchFolderSharingSettingsRequest? = null,
        val updateSharingSettings: WatchFolderSharingSettingsRequest? = null,
    ) : IssueFolderRequest

    @SerialName("IssueTag")
    @Serializable
    data class IssueTag(
        override val name: String? = null,
        val issues: List<IssueRequest>? = null,
        val color: FieldStyleRequest? = null,
        val untagOnResolve: Boolean? = null,
        val visibleFor: UserGroupRequest? = null,
        val updateableBy: UserGroupRequest? = null,
        val readSharingSettings: WatchFolderSharingSettingsRequest? = null,
        val tagSharingSettings: TagSharingSettingsRequest? = null,
        val updateSharingSettings: WatchFolderSharingSettingsRequest? = null,
        val owner: UserRequest? = null,
    ) : IssueFolderRequest

    @SerialName("Tag")
    @Serializable
    data class Tag(
        override val name: String? = null,
        val owner: UserRequest? = null,
        val visibleFor: UserGroupRequest? = null,
        val updateableBy: UserGroupRequest? = null,
        val readSharingSettings: WatchFolderSharingSettingsRequest? = null,
        val updateSharingSettings: WatchFolderSharingSettingsRequest? = null,
        val issues: List<IssueRequest>? = null,
        val color: FieldStyleRequest? = null,
        val untagOnResolve: Boolean? = null,
        val tagSharingSettings: TagSharingSettingsRequest? = null,
    ) : IssueFolderRequest

    @SerialName("SavedQuery")
    @Serializable
    data class SavedQuery(
        override val name: String? = null,
        val owner: UserRequest? = null,
        val visibleFor: UserGroupRequest? = null,
        val updateableBy: UserGroupRequest? = null,
        val readSharingSettings: WatchFolderSharingSettingsRequest? = null,
        val updateSharingSettings: WatchFolderSharingSettingsRequest? = null,
        val query: String? = null,
    ) : IssueFolderRequest

    @SerialName("Project")
    @Serializable
    data class Project(
        override val name: String? = null,
        val archived: Boolean? = null,
        val createdBy: UserRequest? = null,
        val description: String? = null,
        val fromEmail: String? = null,
        val issues: List<IssueRequest>? = null,
        val leader: UserRequest? = null,
        val replyToEmail: String? = null,
        val shortName: String? = null,
        val startingNumber: Long? = null,
        val team: ProjectTeam? = null,
        val template: Boolean? = null,
    ) : IssueFolderRequest
}
