package io.openai.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Optional per-session configuration settings for ChatKit behavior.
 */
@Serializable
public data class ChatkitConfigurationParam(
  @SerialName("automatic_thread_titling")
  public val automaticThreadTitling: AutomaticThreadTitlingParam? = null,
  @SerialName("file_upload")
  public val fileUpload: FileUploadParam? = null,
  public val history: HistoryParam? = null,
)
