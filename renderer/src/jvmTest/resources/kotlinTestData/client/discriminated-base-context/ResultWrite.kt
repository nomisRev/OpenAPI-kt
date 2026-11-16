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

@Serializable(with = ResultWrite.Serializer::class)
public sealed interface ResultWrite {
  @SerialName("item")
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Item.Serializer::class)
  public data class Item(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val item: ItemWrite? = null,
  ) : ResultWrite {
    public object Serializer : TaggedCaseSerializer<Item>(Item.generatedSerializer(), "class", "item")
  }

  public object Serializer : JsonContentPolymorphicSerializer<ResultWrite>(ResultWrite::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ResultWrite> = when (val tag = element.discriminatorValue("class")) {
      "item" -> Item.serializer()
      else -> throw SerializationException("'class' of ResultWrite selects none of item: " + tag)
    }
  }
}
