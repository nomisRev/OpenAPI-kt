package io.openai.model

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class ImageRefParam2(
  @SerialName("image_url")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val imageUrl: String? = null,
  @SerialName("file_id")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val fileId: String? = null,
)
