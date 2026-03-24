package io.openai.model

import kotlin.ByteArray
import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Parameters for creating a new video generation job.
 */
@Serializable
public data class CreateVideoBody(
  public val model: VideoModel? = null,
  public val prompt: String,
  @SerialName("input_reference")
  public val inputReference: ByteArray? = null,
  @SerialName("image_reference")
  public val imageReference: ImageRefParam2? = null,
  public val seconds: VideoSeconds? = null,
  public val size: VideoSize? = null,
)
