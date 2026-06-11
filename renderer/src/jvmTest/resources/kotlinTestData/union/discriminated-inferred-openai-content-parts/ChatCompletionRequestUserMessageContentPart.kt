package io.github.nomisrev.render.test.union.discriminated.inferred.openai.content.parts

import kotlin.OptIn
import kotlin.String
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface ChatCompletionRequestUserMessageContentPart {
  @JvmInline
  @SerialName("text")
  @Serializable
  public value class Text(
    public val text: String,
  ) : ChatCompletionRequestUserMessageContentPart

  @JvmInline
  @SerialName("image_url")
  @Serializable
  public value class ImageUrl(
    public val url: String,
  ) : ChatCompletionRequestUserMessageContentPart
}
