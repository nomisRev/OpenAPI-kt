package io.openai.model

import kotlin.OptIn
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Optional per-session configuration settings for ChatKit behavior.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ChatkitConfigurationParam(
  @SerialName("automatic_thread_titling")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val automaticThreadTitling: AutomaticThreadTitlingParam? = null,
  @SerialName("file_upload")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val fileUpload: FileUploadParam? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val history: HistoryParam? = null,
)
