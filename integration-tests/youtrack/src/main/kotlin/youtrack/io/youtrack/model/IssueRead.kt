package io.youtrack.model

import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class IssueRead(
  public val id: String? = null,
  public val attachments: List<IssueAttachmentRead>? = null,
  public val comments: List<IssueCommentRead>? = null,
  public val commentsCount: Int? = null,
  public val created: Long? = null,
  public val customFields: List<IssueCustomFieldRead>? = null,
  public val description: String? = null,
  public val draftOwner: UserRead? = null,
  public val externalIssue: ExternalIssue? = null,
  public val idReadable: String? = null,
  public val isDraft: Boolean? = null,
  public val links: List<IssueLink>? = null,
  public val numberInProject: Long? = null,
  public val parent: IssueLink? = null,
  public val pinnedComments: List<IssueCommentRead>? = null,
  public val project: IssueFolderRead.Project? = null,
  public val reporter: UserRead? = null,
  public val resolved: Long? = null,
  public val subtasks: IssueLink? = null,
  public val summary: String? = null,
  public val tags: List<IssueFolderRead.Tag>? = null,
  public val updated: Long? = null,
  public val updater: UserRead? = null,
  public val visibility: VisibilityRead? = null,
  public val voters: IssueVoters? = null,
  public val votes: Int? = null,
  public val watchers: IssueWatchers? = null,
  public val wikifiedDescription: String? = null,
  @SerialName("${'$'}type")
  public val type: String? = null,
)
