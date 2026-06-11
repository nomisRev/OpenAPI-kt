package io.github.nomisrev.render.test.collection.item.with.nested.union

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@JvmInline
@Serializable
public value class ModerationInputs(
  public val items: List<Item>,
) {
  @OptIn(ExperimentalSerializationApi::class)
  @JsonClassDiscriminator("type")
  @Serializable
  public sealed interface Item {
    @JvmInline
    @SerialName("image_url")
    @Serializable
    public value class ImageUrl(
      public val url: String,
    ) : Item

    @JvmInline
    @SerialName("text")
    @Serializable
    public value class Text(
      public val text: String,
    ) : Item
  }
}
