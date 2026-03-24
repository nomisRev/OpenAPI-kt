package io.openai.model

import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Output types that you would like the model to generate.
 * Most models are capable of generating text, which is the default:
 *
 * `["text"]`
 *
 * The `gpt-4o-audio-preview` model can also be used to
 * [generate audio](/docs/guides/audio). To request that this model generate
 * both text and audio responses, you can use:
 *
 * `["text", "audio"]`
 *
 */
@JvmInline
@Serializable
public value class ResponseModalities(
  public val items: List<Item>,
) {
  @Serializable
  public enum class Item(
    public val `value`: String,
  ) {
    @SerialName("text")
    Text("text"),
    @SerialName("audio")
    Audio("audio"),
    ;
  }
}
