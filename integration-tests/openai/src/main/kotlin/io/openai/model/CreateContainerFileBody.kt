package io.openai.model

import kotlin.ByteArray
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CreateContainerFileBody(
  @SerialName("file_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val fileId: String? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val `file`: ByteArray? = null,
)
