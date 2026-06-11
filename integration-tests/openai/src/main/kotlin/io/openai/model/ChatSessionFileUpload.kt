package io.openai.model

import kotlin.Boolean
import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Upload permissions and limits applied to the session.
 */
@Serializable
public data class ChatSessionFileUpload(
  public val enabled: Boolean,
  @SerialName("max_file_size")
  public val maxFileSize: Long?,
  @SerialName("max_files")
  public val maxFiles: Long?,
)
