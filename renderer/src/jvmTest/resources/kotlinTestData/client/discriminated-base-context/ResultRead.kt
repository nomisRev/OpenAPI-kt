package io.github.nomisrev.render.test.client.discriminated.base.context

import kotlin.OptIn
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement

@Serializable(with = ResultRead.Serializer::class)
public sealed interface ResultRead {
  @SerialName("item")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Item.Serializer::class)
  public data class Item(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val item: ItemRead? = null,
  ) : ResultRead {
    public object Serializer : TaggedCaseSerializer<Item>(Item.generatedSerializer(), "class", "item")
  }

  public object Serializer : JsonContentPolymorphicSerializer<ResultRead>(ResultRead::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ResultRead> = when (val tag = element.discriminatorValue("class")) {
      "item" -> Item.serializer()
      else -> throw SerializationException("'class' of ResultRead selects none of item: " + tag)
    }
  }
}
