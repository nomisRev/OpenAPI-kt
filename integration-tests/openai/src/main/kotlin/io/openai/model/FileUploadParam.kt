package io.openai.model

import kotlin.Boolean
import kotlin.Long
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Controls whether users can upload files.
 */
@Serializable
public data class FileUploadParam(
  public val enabled: Boolean? = null,
  @SerialName("max_file_size")
  public val maxFileSize: Long? = null,
  @SerialName("max_files")
  public val maxFiles: Long? = null,
)
