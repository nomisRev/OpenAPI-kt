package io.openai.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * ChatKit configuration for the session.
 */
@Serializable
public data class ChatSessionChatkitConfiguration(
  @SerialName("automatic_thread_titling")
  public val automaticThreadTitling: ChatSessionAutomaticThreadTitling,
  @SerialName("file_upload")
  public val fileUpload: ChatSessionFileUpload,
  public val history: ChatSessionHistory,
)
