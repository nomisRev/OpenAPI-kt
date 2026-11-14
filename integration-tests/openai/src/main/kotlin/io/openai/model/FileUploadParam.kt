package io.openai.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Controls whether users can upload files.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class FileUploadParam(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val enabled: Boolean? = null,
  @SerialName("max_file_size")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxFileSize: Long? = null,
  @SerialName("max_files")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val maxFiles: Long? = null,
)
