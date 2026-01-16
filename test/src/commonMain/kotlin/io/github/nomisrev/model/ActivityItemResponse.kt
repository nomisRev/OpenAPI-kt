package io.github.nomisrev.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator($$"$type")
@Serializable
sealed interface ActivityItemResponse {
    val id: String?
    val author: UserResponse?
    val category: ActivityCategoryResponse?
    val field: FilterFieldResponse?
    val targetMember: String?
    val timestamp: Long?

    @SerialName("Default")
    @Serializable
    data class Default(
        override val id: String? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
    ) : ActivityItemResponse

    @SerialName("VcsChangeActivityItem")
    @Serializable
    data class VcsChangeActivityItem(
        override val id: String? = null,
        val added: List<VcsChangeResponse>? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: List<VcsChangeResponse>? = null,
        val target: JsonElement? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
    ) : ActivityItemResponse

    @SerialName("WorkItemActivityItem")
    @Serializable
    data class WorkItemActivityItem(
        override val id: String? = null,
        val added: List<IssueWorkItem>? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: List<IssueWorkItem>? = null,
        val target: IssueWorkItem? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
    ) : ActivityItemResponse

    @SerialName("WorkItemAuthorActivityItem")
    @Serializable
    data class WorkItemAuthorActivityItem(
        override val id: String? = null,
        val added: UserResponse? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: UserResponse? = null,
        val target: IssueWorkItem? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
    ) : ActivityItemResponse

    @SerialName("WorkItemDurationActivityItem")
    @Serializable
    data class WorkItemDurationActivityItem(
        override val id: String? = null,
        val added: DurationValueResponse? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: DurationValueResponse? = null,
        val target: IssueWorkItem? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
    ) : ActivityItemResponse

    @SerialName("WorkItemTypeActivityItem")
    @Serializable
    data class WorkItemTypeActivityItem(
        override val id: String? = null,
        val added: List<WorkItemTypeResponse>? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: List<WorkItemTypeResponse>? = null,
        val target: IssueWorkItem? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
    ) : ActivityItemResponse

    @SerialName("SprintActivityItem")
    @Serializable
    data class SprintActivityItem(
        override val id: String? = null,
        val added: List<SprintResponse>? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: List<SprintResponse>? = null,
        val target: IssueResponse? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
    ) : ActivityItemResponse

    @SerialName("CreatedDeletedActivityItem")
    @Serializable
    data class CreatedDeletedActivityItem(
        override val id: String? = null,
        val added: JsonElement? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: JsonElement? = null,
        val target: JsonElement? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
    ) : ActivityItemResponse

    @SerialName("AttachmentActivityItem")
    @Serializable
    data class AttachmentActivityItem(
        override val id: String? = null,
        val added: List<IssueAttachmentResponse>? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: List<IssueAttachmentResponse>? = null,
        val target: IssueAttachmentResponse? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
    ) : ActivityItemResponse

    @SerialName("CommentActivityItem")
    @Serializable
    data class CommentActivityItem(
        override val id: String? = null,
        val added: List<IssueCommentResponse>? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: List<IssueCommentResponse>? = null,
        val target: IssueCommentResponse? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
        val authorGroup: UserGroupResponse? = null,
    ) : ActivityItemResponse

    @SerialName("IssueCreatedActivityItem")
    @Serializable
    data class IssueCreatedActivityItem(
        override val id: String? = null,
        val added: List<IssueResponse>? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: List<IssueResponse>? = null,
        val target: IssueResponse? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
    ) : ActivityItemResponse

    @SerialName("IssueResolvedActivityItem")
    @Serializable
    data class IssueResolvedActivityItem(
        override val id: String? = null,
        val added: Long? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: Long? = null,
        val target: JsonElement? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
    ) : ActivityItemResponse

    @SerialName("SingleValueActivityItem")
    @Serializable
    data class SingleValueActivityItem(
        override val id: String? = null,
        val added: JsonElement? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: JsonElement? = null,
        val target: JsonElement? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
    ) : ActivityItemResponse

    @SerialName("SimpleValueActivityItem")
    @Serializable
    data class SimpleValueActivityItem(
        override val id: String? = null,
        val added: JsonElement? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: JsonElement? = null,
        val target: JsonElement? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
    ) : ActivityItemResponse

    @SerialName("TextMarkupActivityItem")
    @Serializable
    data class TextMarkupActivityItem(
        override val id: String? = null,
        val added: String? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: String? = null,
        val target: JsonElement? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
        val markup: String? = null,
    ) : ActivityItemResponse

    @SerialName("UsesMarkupActivityItem")
    @Serializable
    data class UsesMarkupActivityItem(
        override val id: String? = null,
        val added: Boolean? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: Boolean? = null,
        val target: JsonElement? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
        val markup: String? = null,
    ) : ActivityItemResponse

    @SerialName("ProjectActivityItem")
    @Serializable
    data class ProjectActivityItem(
        override val id: String? = null,
        val added: IssueKeyResponse? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: IssueKeyResponse? = null,
        val target: IssueResponse? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
    ) : ActivityItemResponse

    @SerialName("MultiValueActivityItem")
    @Serializable
    data class MultiValueActivityItem(
        override val id: String? = null,
        val added: JsonElement? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: JsonElement? = null,
        val target: JsonElement? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
    ) : ActivityItemResponse

    @SerialName("CommentAttachmentsActivityItem")
    @Serializable
    data class CommentAttachmentsActivityItem(
        override val id: String? = null,
        val added: List<IssueAttachmentResponse>? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: List<IssueAttachmentResponse>? = null,
        val target: IssueCommentResponse? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
    ) : ActivityItemResponse

    @SerialName("LinksActivityItem")
    @Serializable
    data class LinksActivityItem(
        override val id: String? = null,
        val added: List<IssueResponse>? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: List<IssueResponse>? = null,
        val target: IssueResponse? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
    ) : ActivityItemResponse

    @SerialName("TagsActivityItem")
    @Serializable
    data class TagsActivityItem(
        override val id: String? = null,
        val added: List<Tag>? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: List<Tag>? = null,
        val target: IssueResponse? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
    ) : ActivityItemResponse

    @SerialName("VotersActivityItem")
    @Serializable
    data class VotersActivityItem(
        override val id: String? = null,
        val added: List<UserResponse>? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: List<UserResponse>? = null,
        val target: IssueResponse? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
    ) : ActivityItemResponse

    @SerialName("VisibilityActivityItem")
    @Serializable
    data class VisibilityActivityItem(
        override val id: String? = null,
        val added: JsonElement? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: JsonElement? = null,
        val target: JsonElement? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
        val targetSubMember: String? = null,
    ) : ActivityItemResponse

    @SerialName("VisibilityGroupActivityItem")
    @Serializable
    data class VisibilityGroupActivityItem(
        override val id: String? = null,
        val added: List<UserGroupResponse>? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: List<UserGroupResponse>? = null,
        val target: JsonElement? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
        val targetSubMember: String? = null,
    ) : ActivityItemResponse

    @SerialName("VisibilityUserActivityItem")
    @Serializable
    data class VisibilityUserActivityItem(
        override val id: String? = null,
        val added: List<UserResponse>? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: List<UserResponse>? = null,
        val target: JsonElement? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
        val targetSubMember: String? = null,
    ) : ActivityItemResponse

    @SerialName("CustomFieldActivityItem")
    @Serializable
    data class CustomFieldActivityItem(
        override val id: String? = null,
        val added: JsonElement? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: JsonElement? = null,
        val target: IssueResponse? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
    ) : ActivityItemResponse

    @SerialName("TextCustomFieldActivityItem")
    @Serializable
    data class TextCustomFieldActivityItem(
        override val id: String? = null,
        val added: String? = null,
        override val author: UserResponse? = null,
        override val category: ActivityCategoryResponse? = null,
        override val field: FilterFieldResponse? = null,
        val removed: String? = null,
        val target: IssueResponse? = null,
        override val targetMember: String? = null,
        override val timestamp: Long? = null,
        val markup: String? = null,
    ) : ActivityItemResponse
}
