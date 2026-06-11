package io.github.nomisrev.render.test.union.discriminated.tagged.custom.multivalue

import kotlin.Int
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializable(with = Entry.Serializer::class)
public sealed interface Entry {
  @Serializable
  public data class DirectoryEntry(
    public val type: Type,
    public val children: List<String>,
  ) : Entry {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("dir")
      Dir("dir"),
      @SerialName("folder")
      Folder("folder"),
      ;
    }
  }

  @Serializable
  public data class File(
    public val type: Type,
    public val size: Int,
  ) : Entry {
    @Serializable
    public enum class Type(
      public val `value`: String,
    ) {
      @SerialName("file")
      File("file"),
      ;
    }
  }

  public object Serializer : KSerializer<Entry> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.union.discriminated.tagged.custom.multivalue.Entry", PolymorphicKind.SEALED) {
      element("DirectoryEntry", DirectoryEntry.serializer().descriptor)
      element("File", File.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): Entry {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      val obj = value as? JsonObject
      val tag = (obj?.get("type") as? JsonPrimitive)?.content
      when(tag) {
        "dir", "folder" -> return json.decodeFromJsonElement(DirectoryEntry.serializer(), value)
        "file" -> return json.decodeFromJsonElement(File.serializer(), value)
        else -> throw SerializationException("Unknown tag: " + tag + " for io.github.nomisrev.render.test.union.discriminated.tagged.custom.multivalue.Entry")
      }
    }

    override fun serialize(encoder: Encoder, `value`: Entry) {
      when(value) {
        is DirectoryEntry -> encoder.encodeSerializableValue(DirectoryEntry.serializer(), value)
        is File -> encoder.encodeSerializableValue(File.serializer(), value)
      }
    }
  }
}
