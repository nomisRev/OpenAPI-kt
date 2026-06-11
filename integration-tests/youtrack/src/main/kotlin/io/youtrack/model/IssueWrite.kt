package io.youtrack.model

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

@Serializable
public data class IssueWrite(
  public val attachments: List<IssueAttachmentWrite>? = null,
  public val comments: List<IssueCommentWrite>? = null,
  public val description: String? = null,
  public val project: IssueFolderWrite.Project? = null,
  public val summary: String? = null,
  public val tags: List<IssueFolderWrite.Tag>? = null,
  public val visibility: VisibilityWrite? = null,
)
