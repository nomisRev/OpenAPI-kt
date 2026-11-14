package io.youtrack.model

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class IssueWrite(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val attachments: List<IssueAttachmentWrite>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val comments: List<IssueCommentWrite>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val description: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val project: IssueFolderWrite.Project? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val summary: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val tags: List<IssueFolderWrite.Tag>? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val visibility: VisibilityWrite? = null,
)
