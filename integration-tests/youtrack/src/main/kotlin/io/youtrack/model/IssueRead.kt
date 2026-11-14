package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class IssueRead(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val id: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val attachments: List<IssueAttachmentRead>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val comments: List<IssueCommentRead>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val commentsCount: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val created: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val customFields: List<IssueCustomFieldRead>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val description: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val draftOwner: UserRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val externalIssue: ExternalIssue? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val idReadable: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val isDraft: Boolean? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val links: List<IssueLink>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val numberInProject: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val parent: IssueLink? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val pinnedComments: List<IssueCommentRead>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val project: IssueFolderRead.Project? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val reporter: UserRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val resolved: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val subtasks: IssueLink? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val summary: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val tags: List<IssueFolderRead.Tag>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val updated: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val updater: UserRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val visibility: VisibilityRead? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val voters: IssueVoters? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val votes: Int? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val watchers: IssueWatchers? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val wikifiedDescription: String? = null,
  @SerialName("${'$'}type")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val type: String? = null,
)
