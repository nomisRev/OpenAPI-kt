package io.github.nomisrev.render.test.union.discriminated.inferred.openai.content.parts

import kotlin.OptIn
import kotlin.String
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface ChatCompletionRequestUserMessageContentPart {
  @SerialName("text")
  @Serializable
  public data class Text(
    public val text: String,
  ) : ChatCompletionRequestUserMessageContentPart

  @SerialName("image_url")
  @Serializable
  public data class ImageUrl(
    public val url: String,
  ) : ChatCompletionRequestUserMessageContentPart
}
