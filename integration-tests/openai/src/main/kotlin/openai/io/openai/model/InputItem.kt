package io.openai.model

import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@JsonClassDiscriminator("type")
@Serializable
public sealed interface InputItem {
  @Serializable
  @JvmInline
  @SerialName("EasyInputMessage")
  public value class EasyInputMessage(
    public val `value`: io.openai.model.EasyInputMessage,
  ) : InputItem

  @Serializable
  @JvmInline
  @SerialName("Item")
  public value class Item(
    public val `value`: io.openai.model.Item,
  ) : InputItem

  @Serializable
  @JvmInline
  @SerialName("ItemReferenceParam")
  public value class ItemReferenceParam(
    public val `value`: io.openai.model.ItemReferenceParam,
  ) : InputItem
}
