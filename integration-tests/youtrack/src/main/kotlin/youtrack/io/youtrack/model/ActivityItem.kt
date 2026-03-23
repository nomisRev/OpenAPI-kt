package io.youtrack.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.json.JsonElement

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("${'$'}type")
@Serializable
public sealed interface ActivityItem {
  public val id: String?

  public val author: UserRead?

  public val category: ActivityCategory?

  public val `field`: FilterFieldRead?

  public val targetMember: String?

  public val timestamp: Long?

  @SerialName("Default")
  @Serializable
  public data class Default(
    override val id: String? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("VcsChangeActivityItem")
  @Serializable
  public data class VcsChangeActivityItem(
    override val id: String? = null,
    public val added: List<VcsChangeRead>? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: List<VcsChangeRead>? = null,
    public val target: JsonElement? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("WorkItemActivityItem")
  @Serializable
  public data class WorkItemActivityItem(
    override val id: String? = null,
    public val added: List<BaseWorkItemRead.IssueWorkItem>? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: List<BaseWorkItemRead.IssueWorkItem>? = null,
    public val target: BaseWorkItemRead.IssueWorkItem? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("WorkItemAuthorActivityItem")
  @Serializable
  public data class WorkItemAuthorActivityItem(
    override val id: String? = null,
    public val added: UserRead? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: UserRead? = null,
    public val target: BaseWorkItemRead.IssueWorkItem? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("WorkItemDurationActivityItem")
  @Serializable
  public data class WorkItemDurationActivityItem(
    override val id: String? = null,
    public val added: DurationValueRead? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: DurationValueRead? = null,
    public val target: BaseWorkItemRead.IssueWorkItem? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("WorkItemTypeActivityItem")
  @Serializable
  public data class WorkItemTypeActivityItem(
    override val id: String? = null,
    public val added: List<WorkItemTypeRead>? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: List<WorkItemTypeRead>? = null,
    public val target: BaseWorkItemRead.IssueWorkItem? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("SprintActivityItem")
  @Serializable
  public data class SprintActivityItem(
    override val id: String? = null,
    public val added: List<SprintRead>? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: List<SprintRead>? = null,
    public val target: IssueRead? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("CreatedDeletedActivityItem")
  @Serializable
  public data class CreatedDeletedActivityItem(
    override val id: String? = null,
    public val added: JsonElement? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: JsonElement? = null,
    public val target: JsonElement? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("AttachmentActivityItem")
  @Serializable
  public data class AttachmentActivityItem(
    override val id: String? = null,
    public val added: List<IssueAttachmentRead>? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: List<IssueAttachmentRead>? = null,
    public val target: IssueAttachmentRead? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("CommentActivityItem")
  @Serializable
  public data class CommentActivityItem(
    override val id: String? = null,
    public val added: List<IssueCommentRead>? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: List<IssueCommentRead>? = null,
    public val target: IssueCommentRead? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
    public val authorGroup: UserGroupRead? = null,
  ) : ActivityItem

  @SerialName("IssueCreatedActivityItem")
  @Serializable
  public data class IssueCreatedActivityItem(
    override val id: String? = null,
    public val added: List<IssueRead>? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: List<IssueRead>? = null,
    public val target: IssueRead? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("IssueResolvedActivityItem")
  @Serializable
  public data class IssueResolvedActivityItem(
    override val id: String? = null,
    public val added: Long? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: Long? = null,
    public val target: JsonElement? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("SingleValueActivityItem")
  @Serializable
  public data class SingleValueActivityItem(
    override val id: String? = null,
    public val added: JsonElement? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: JsonElement? = null,
    public val target: JsonElement? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("SimpleValueActivityItem")
  @Serializable
  public data class SimpleValueActivityItem(
    override val id: String? = null,
    public val added: JsonElement? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: JsonElement? = null,
    public val target: JsonElement? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("TextMarkupActivityItem")
  @Serializable
  public data class TextMarkupActivityItem(
    override val id: String? = null,
    public val added: String? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: String? = null,
    public val target: JsonElement? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
    public val markup: String? = null,
  ) : ActivityItem

  @SerialName("UsesMarkupActivityItem")
  @Serializable
  public data class UsesMarkupActivityItem(
    override val id: String? = null,
    public val added: Boolean? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: Boolean? = null,
    public val target: JsonElement? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
    public val markup: String? = null,
  ) : ActivityItem

  @SerialName("ProjectActivityItem")
  @Serializable
  public data class ProjectActivityItem(
    override val id: String? = null,
    public val added: IssueKey? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: IssueKey? = null,
    public val target: IssueRead? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("MultiValueActivityItem")
  @Serializable
  public data class MultiValueActivityItem(
    override val id: String? = null,
    public val added: JsonElement? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: JsonElement? = null,
    public val target: JsonElement? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("CommentAttachmentsActivityItem")
  @Serializable
  public data class CommentAttachmentsActivityItem(
    override val id: String? = null,
    public val added: List<IssueAttachmentRead>? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: List<IssueAttachmentRead>? = null,
    public val target: IssueCommentRead? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("LinksActivityItem")
  @Serializable
  public data class LinksActivityItem(
    override val id: String? = null,
    public val added: List<IssueRead>? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: List<IssueRead>? = null,
    public val target: IssueRead? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("TagsActivityItem")
  @Serializable
  public data class TagsActivityItem(
    override val id: String? = null,
    public val added: List<IssueFolderRead.Tag>? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: List<IssueFolderRead.Tag>? = null,
    public val target: IssueRead? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("VotersActivityItem")
  @Serializable
  public data class VotersActivityItem(
    override val id: String? = null,
    public val added: List<UserRead>? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: List<UserRead>? = null,
    public val target: IssueRead? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("VisibilityActivityItem")
  @Serializable
  public data class VisibilityActivityItem(
    override val id: String? = null,
    public val added: JsonElement? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: JsonElement? = null,
    public val target: JsonElement? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
    public val targetSubMember: String? = null,
  ) : ActivityItem

  @SerialName("VisibilityGroupActivityItem")
  @Serializable
  public data class VisibilityGroupActivityItem(
    override val id: String? = null,
    public val added: List<UserGroupRead>? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: List<UserGroupRead>? = null,
    public val target: JsonElement? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
    public val targetSubMember: String? = null,
  ) : ActivityItem

  @SerialName("VisibilityUserActivityItem")
  @Serializable
  public data class VisibilityUserActivityItem(
    override val id: String? = null,
    public val added: List<UserRead>? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: List<UserRead>? = null,
    public val target: JsonElement? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
    public val targetSubMember: String? = null,
  ) : ActivityItem

  @SerialName("CustomFieldActivityItem")
  @Serializable
  public data class CustomFieldActivityItem(
    override val id: String? = null,
    public val added: JsonElement? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: JsonElement? = null,
    public val target: IssueRead? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("TextCustomFieldActivityItem")
  @Serializable
  public data class TextCustomFieldActivityItem(
    override val id: String? = null,
    public val added: String? = null,
    override val author: UserRead? = null,
    override val category: ActivityCategory? = null,
    override val `field`: FilterFieldRead? = null,
    public val removed: String? = null,
    public val target: IssueRead? = null,
    override val targetMember: String? = null,
    override val timestamp: Long? = null,
    public val markup: String? = null,
  ) : ActivityItem
}
