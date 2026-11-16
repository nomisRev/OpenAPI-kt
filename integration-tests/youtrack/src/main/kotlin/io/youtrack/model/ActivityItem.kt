package io.youtrack.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement

@Serializable(with = ActivityItem.Serializer::class)
public sealed interface ActivityItem {
  public val id: String?

  public val author: UserRead?

  public val category: ActivityCategory?

  public val `field`: FilterFieldRead?

  public val targetMember: String?

  public val timestamp: Long?

  @SerialName("ActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Default.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<Default>(Default.generatedSerializer(), "${'$'}type", "ActivityItem")
  }

  @SerialName("VcsChangeActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = VcsChangeActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<VcsChangeActivityItem>(VcsChangeActivityItem.generatedSerializer(), "${'$'}type", "VcsChangeActivityItem")
  }

  @SerialName("WorkItemActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = WorkItemActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<WorkItemActivityItem>(WorkItemActivityItem.generatedSerializer(), "${'$'}type", "WorkItemActivityItem")
  }

  @SerialName("WorkItemAuthorActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = WorkItemAuthorActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<WorkItemAuthorActivityItem>(WorkItemAuthorActivityItem.generatedSerializer(), "${'$'}type", "WorkItemAuthorActivityItem")
  }

  @SerialName("WorkItemDurationActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = WorkItemDurationActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<WorkItemDurationActivityItem>(WorkItemDurationActivityItem.generatedSerializer(), "${'$'}type", "WorkItemDurationActivityItem")
  }

  @SerialName("WorkItemTypeActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = WorkItemTypeActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<WorkItemTypeActivityItem>(WorkItemTypeActivityItem.generatedSerializer(), "${'$'}type", "WorkItemTypeActivityItem")
  }

  @SerialName("SprintActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SprintActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<SprintActivityItem>(SprintActivityItem.generatedSerializer(), "${'$'}type", "SprintActivityItem")
  }

  @SerialName("CreatedDeletedActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = CreatedDeletedActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<CreatedDeletedActivityItem>(CreatedDeletedActivityItem.generatedSerializer(), "${'$'}type", "CreatedDeletedActivityItem")
  }

  @SerialName("AttachmentActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = AttachmentActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<AttachmentActivityItem>(AttachmentActivityItem.generatedSerializer(), "${'$'}type", "AttachmentActivityItem")
  }

  @SerialName("CommentActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = CommentActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<CommentActivityItem>(CommentActivityItem.generatedSerializer(), "${'$'}type", "CommentActivityItem")
  }

  @SerialName("IssueCreatedActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = IssueCreatedActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<IssueCreatedActivityItem>(IssueCreatedActivityItem.generatedSerializer(), "${'$'}type", "IssueCreatedActivityItem")
  }

  @SerialName("IssueResolvedActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = IssueResolvedActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<IssueResolvedActivityItem>(IssueResolvedActivityItem.generatedSerializer(), "${'$'}type", "IssueResolvedActivityItem")
  }

  @SerialName("SingleValueActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SingleValueActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<SingleValueActivityItem>(SingleValueActivityItem.generatedSerializer(), "${'$'}type", "SingleValueActivityItem")
  }

  @SerialName("SimpleValueActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = SimpleValueActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<SimpleValueActivityItem>(SimpleValueActivityItem.generatedSerializer(), "${'$'}type", "SimpleValueActivityItem")
  }

  @SerialName("TextMarkupActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = TextMarkupActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<TextMarkupActivityItem>(TextMarkupActivityItem.generatedSerializer(), "${'$'}type", "TextMarkupActivityItem")
  }

  @SerialName("UsesMarkupActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = UsesMarkupActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<UsesMarkupActivityItem>(UsesMarkupActivityItem.generatedSerializer(), "${'$'}type", "UsesMarkupActivityItem")
  }

  @SerialName("ProjectActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = ProjectActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<ProjectActivityItem>(ProjectActivityItem.generatedSerializer(), "${'$'}type", "ProjectActivityItem")
  }

  @SerialName("MultiValueActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = MultiValueActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<MultiValueActivityItem>(MultiValueActivityItem.generatedSerializer(), "${'$'}type", "MultiValueActivityItem")
  }

  @SerialName("CommentAttachmentsActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = CommentAttachmentsActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<CommentAttachmentsActivityItem>(CommentAttachmentsActivityItem.generatedSerializer(), "${'$'}type", "CommentAttachmentsActivityItem")
  }

  @SerialName("LinksActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = LinksActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<LinksActivityItem>(LinksActivityItem.generatedSerializer(), "${'$'}type", "LinksActivityItem")
  }

  @SerialName("TagsActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = TagsActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<TagsActivityItem>(TagsActivityItem.generatedSerializer(), "${'$'}type", "TagsActivityItem")
  }

  @SerialName("VotersActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = VotersActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<VotersActivityItem>(VotersActivityItem.generatedSerializer(), "${'$'}type", "VotersActivityItem")
  }

  @SerialName("VisibilityActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = VisibilityActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<VisibilityActivityItem>(VisibilityActivityItem.generatedSerializer(), "${'$'}type", "VisibilityActivityItem")
  }

  @SerialName("VisibilityGroupActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = VisibilityGroupActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<VisibilityGroupActivityItem>(VisibilityGroupActivityItem.generatedSerializer(), "${'$'}type", "VisibilityGroupActivityItem")
  }

  @SerialName("VisibilityUserActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = VisibilityUserActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<VisibilityUserActivityItem>(VisibilityUserActivityItem.generatedSerializer(), "${'$'}type", "VisibilityUserActivityItem")
  }

  @SerialName("CustomFieldActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = CustomFieldActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<CustomFieldActivityItem>(CustomFieldActivityItem.generatedSerializer(), "${'$'}type", "CustomFieldActivityItem")
  }

  @SerialName("TextCustomFieldActivityItem")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = TextCustomFieldActivityItem.Serializer::class)
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
  ) : ActivityItem {
    public object Serializer : TaggedCaseSerializer<TextCustomFieldActivityItem>(TextCustomFieldActivityItem.generatedSerializer(), "${'$'}type", "TextCustomFieldActivityItem")
  }

  public object Serializer : JsonContentPolymorphicSerializer<ActivityItem>(ActivityItem::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ActivityItem> = when (val tag = element.discriminatorValue("${'$'}type")) {
      "ActivityItem" -> Default.serializer()
      "VcsChangeActivityItem" -> VcsChangeActivityItem.serializer()
      "WorkItemActivityItem" -> WorkItemActivityItem.serializer()
      "WorkItemAuthorActivityItem" -> WorkItemAuthorActivityItem.serializer()
      "WorkItemDurationActivityItem" -> WorkItemDurationActivityItem.serializer()
      "WorkItemTypeActivityItem" -> WorkItemTypeActivityItem.serializer()
      "SprintActivityItem" -> SprintActivityItem.serializer()
      "CreatedDeletedActivityItem" -> CreatedDeletedActivityItem.serializer()
      "AttachmentActivityItem" -> AttachmentActivityItem.serializer()
      "CommentActivityItem" -> CommentActivityItem.serializer()
      "IssueCreatedActivityItem" -> IssueCreatedActivityItem.serializer()
      "IssueResolvedActivityItem" -> IssueResolvedActivityItem.serializer()
      "SingleValueActivityItem" -> SingleValueActivityItem.serializer()
      "SimpleValueActivityItem" -> SimpleValueActivityItem.serializer()
      "TextMarkupActivityItem" -> TextMarkupActivityItem.serializer()
      "UsesMarkupActivityItem" -> UsesMarkupActivityItem.serializer()
      "ProjectActivityItem" -> ProjectActivityItem.serializer()
      "MultiValueActivityItem" -> MultiValueActivityItem.serializer()
      "CommentAttachmentsActivityItem" -> CommentAttachmentsActivityItem.serializer()
      "LinksActivityItem" -> LinksActivityItem.serializer()
      "TagsActivityItem" -> TagsActivityItem.serializer()
      "VotersActivityItem" -> VotersActivityItem.serializer()
      "VisibilityActivityItem" -> VisibilityActivityItem.serializer()
      "VisibilityGroupActivityItem" -> VisibilityGroupActivityItem.serializer()
      "VisibilityUserActivityItem" -> VisibilityUserActivityItem.serializer()
      "CustomFieldActivityItem" -> CustomFieldActivityItem.serializer()
      "TextCustomFieldActivityItem" -> TextCustomFieldActivityItem.serializer()
      else -> throw SerializationException("'${'$'}type' of ActivityItem selects none of ActivityItem, VcsChangeActivityItem, WorkItemActivityItem, WorkItemAuthorActivityItem, WorkItemDurationActivityItem, WorkItemTypeActivityItem, SprintActivityItem, CreatedDeletedActivityItem, AttachmentActivityItem, CommentActivityItem, IssueCreatedActivityItem, IssueResolvedActivityItem, SingleValueActivityItem, SimpleValueActivityItem, TextMarkupActivityItem, UsesMarkupActivityItem, ProjectActivityItem, MultiValueActivityItem, CommentAttachmentsActivityItem, LinksActivityItem, TagsActivityItem, VotersActivityItem, VisibilityActivityItem, VisibilityGroupActivityItem, VisibilityUserActivityItem, CustomFieldActivityItem, TextCustomFieldActivityItem: " + tag)
    }
  }
}
