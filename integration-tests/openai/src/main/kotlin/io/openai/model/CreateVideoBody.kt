package io.openai.model

import kotlin.ByteArray
import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Parameters for creating a new video generation job.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class CreateVideoBody(
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val model: VideoModel? = null,
  public val prompt: String,
  @SerialName("input_reference")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val inputReference: ByteArray? = null,
  @SerialName("image_reference")
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val imageReference: ImageRefParam2? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val seconds: VideoSeconds? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val size: VideoSize? = null,
)
