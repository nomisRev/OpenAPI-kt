package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface IssueFolderResponse {
    val id: String?
    val name: String?

    @SerialName("Default")
    @Serializable
    data class Default(override val id: String? = null, override val name: String? = null) : IssueFolderResponse

    @SerialName("WatchFolder")
    @Serializable
    data class WatchFolder(
        override val id: String? = null,
        override val name: String? = null,
        val owner: UserResponse? = null,
        val visibleFor: UserGroupResponse? = null,
        val updateableBy: UserGroupResponse? = null,
        val readSharingSettings: WatchFolderSharingSettingsResponse? = null,
        val updateSharingSettings: WatchFolderSharingSettingsResponse? = null,
    ) : IssueFolderResponse

    @SerialName("IssueTag")
    @Serializable
    data class IssueTag(
        override val id: String? = null,
        override val name: String? = null,
        val issues: List<IssueResponse>? = null,
        val color: FieldStyleResponse? = null,
        val untagOnResolve: Boolean? = null,
        val visibleFor: UserGroupResponse? = null,
        val updateableBy: UserGroupResponse? = null,
        val readSharingSettings: WatchFolderSharingSettingsResponse? = null,
        val tagSharingSettings: TagSharingSettingsResponse? = null,
        val updateSharingSettings: WatchFolderSharingSettingsResponse? = null,
        val owner: UserResponse? = null,
    ) : IssueFolderResponse

    @SerialName("Tag")
    @Serializable
    data class Tag(
        override val id: String? = null,
        override val name: String? = null,
        val owner: UserResponse? = null,
        val visibleFor: UserGroupResponse? = null,
        val updateableBy: UserGroupResponse? = null,
        val readSharingSettings: WatchFolderSharingSettingsResponse? = null,
        val updateSharingSettings: WatchFolderSharingSettingsResponse? = null,
        val issues: List<IssueResponse>? = null,
        val color: FieldStyleResponse? = null,
        val untagOnResolve: Boolean? = null,
        val tagSharingSettings: TagSharingSettingsResponse? = null,
    ) : IssueFolderResponse

    @SerialName("SavedQuery")
    @Serializable
    data class SavedQuery(
        override val id: String? = null,
        override val name: String? = null,
        val owner: UserResponse? = null,
        val visibleFor: UserGroupResponse? = null,
        val updateableBy: UserGroupResponse? = null,
        val readSharingSettings: WatchFolderSharingSettingsResponse? = null,
        val updateSharingSettings: WatchFolderSharingSettingsResponse? = null,
        val query: String? = null,
        val issues: List<IssueResponse>? = null,
    ) : IssueFolderResponse

    @SerialName("Project")
    @Serializable
    data class Project(
        override val id: String? = null,
        override val name: String? = null,
        val archived: Boolean? = null,
        val createdBy: UserResponse? = null,
        val customFields: JsonElement? = null,
        val description: String? = null,
        val fromEmail: String? = null,
        val iconUrl: String? = null,
        val issues: List<IssueResponse>? = null,
        val leader: UserResponse? = null,
        val replyToEmail: String? = null,
        val shortName: String? = null,
        val startingNumber: Long? = null,
        val team: ProjectTeam? = null,
        val template: Boolean? = null,
    ) : IssueFolderResponse
}
