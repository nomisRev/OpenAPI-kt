package io.youtrack.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
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

  @SerialName("ActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Default(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("VcsChangeActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class VcsChangeActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: List<VcsChangeRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: List<VcsChangeRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("WorkItemActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class WorkItemActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: List<BaseWorkItemRead.IssueWorkItem>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: List<BaseWorkItemRead.IssueWorkItem>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: BaseWorkItemRead.IssueWorkItem? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("WorkItemAuthorActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class WorkItemAuthorActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: BaseWorkItemRead.IssueWorkItem? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("WorkItemDurationActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class WorkItemDurationActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: DurationValueRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: DurationValueRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: BaseWorkItemRead.IssueWorkItem? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("WorkItemTypeActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class WorkItemTypeActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: List<WorkItemTypeRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: List<WorkItemTypeRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: BaseWorkItemRead.IssueWorkItem? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("SprintActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SprintActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: List<SprintRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: List<SprintRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: IssueRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("CreatedDeletedActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class CreatedDeletedActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("AttachmentActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class AttachmentActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: List<IssueAttachmentRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: List<IssueAttachmentRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: IssueAttachmentRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("CommentActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class CommentActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: List<IssueCommentRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: List<IssueCommentRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: IssueCommentRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val authorGroup: UserGroupRead? = null,
  ) : ActivityItem

  @SerialName("IssueCreatedActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class IssueCreatedActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: List<IssueRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: List<IssueRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: IssueRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("IssueResolvedActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class IssueResolvedActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("SingleValueActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SingleValueActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("SimpleValueActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class SimpleValueActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("TextMarkupActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class TextMarkupActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val markup: String? = null,
  ) : ActivityItem

  @SerialName("UsesMarkupActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class UsesMarkupActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: Boolean? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val markup: String? = null,
  ) : ActivityItem

  @SerialName("ProjectActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class ProjectActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: IssueKey? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: IssueKey? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: IssueRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("MultiValueActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class MultiValueActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("CommentAttachmentsActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class CommentAttachmentsActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: List<IssueAttachmentRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: List<IssueAttachmentRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: IssueCommentRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("LinksActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class LinksActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: List<IssueRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: List<IssueRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: IssueRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("TagsActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class TagsActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: List<IssueFolderRead.Tag>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: List<IssueFolderRead.Tag>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: IssueRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("VotersActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class VotersActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: List<UserRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: List<UserRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: IssueRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("VisibilityActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class VisibilityActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val targetSubMember: String? = null,
  ) : ActivityItem

  @SerialName("VisibilityGroupActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class VisibilityGroupActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: List<UserGroupRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: List<UserGroupRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val targetSubMember: String? = null,
  ) : ActivityItem

  @SerialName("VisibilityUserActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class VisibilityUserActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: List<UserRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: List<UserRead>? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val targetSubMember: String? = null,
  ) : ActivityItem

  @SerialName("CustomFieldActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class CustomFieldActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: JsonElement? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: IssueRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
  ) : ActivityItem

  @SerialName("TextCustomFieldActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class TextCustomFieldActivityItem(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val id: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val added: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val author: UserRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val category: ActivityCategory? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val `field`: FilterFieldRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val removed: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val target: IssueRead? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val targetMember: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    override val timestamp: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val markup: String? = null,
  ) : ActivityItem
}
