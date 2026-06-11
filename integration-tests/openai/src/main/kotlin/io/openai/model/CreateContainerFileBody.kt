package io.openai.model

import kotlin.ByteArray
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CreateContainerFileBody(
  @SerialName("file_id")
  public val fileId: String? = null,
  public val `file`: ByteArray? = null,
)
