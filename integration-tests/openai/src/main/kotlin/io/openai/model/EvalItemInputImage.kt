package io.openai.model

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * An image input block used within EvalItem content arrays.
 */
@Serializable
public data class EvalItemInputImage(
  public val type: Type,
  @SerialName("image_url")
  public val imageUrl: String,
  public val detail: String? = null,
) {
  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("input_image")
    InputImage("input_image"),
    ;
  }
}
