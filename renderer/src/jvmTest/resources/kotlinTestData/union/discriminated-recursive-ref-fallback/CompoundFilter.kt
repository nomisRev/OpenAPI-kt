package io.github.nomisrev.render.test.union.discriminated.recursive.ref.fallback

import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
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

@Serializable
public data class CompoundFilter(
  public val type: Type,
  public val filters: List<Filters>,
) {
  @Serializable(with = Filters.Serializer::class)
  public sealed interface Filters {
    @Serializable
    public data class ComparisonFilter(
      public val type: Type,
      public val key: String,
      public val `value`: String,
    ) : Filters {
      @Serializable
      public enum class Type(
        public val `value`: String,
      ) {
        @SerialName("eq")
        Eq("eq"),
        @SerialName("ne")
        Ne("ne"),
        ;
      }
    }

    @Serializable
    @JvmInline
    public value class CaseCompoundFilter(
      public val `value`: CompoundFilter,
    ) : Filters

    public object Serializer : KSerializer<Filters> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.github.nomisrev.render.test.union.discriminated.recursive.ref.fallback.CompoundFilter.Filters", PolymorphicKind.SEALED) {
        element("ComparisonFilter", ComparisonFilter.serializer().descriptor)
        element("CaseCompoundFilter", CompoundFilter.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): Filters {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        val obj = value as? JsonObject
        val tag = (obj?.get("type") as? JsonPrimitive)?.content
        when(tag) {
          "eq", "ne" -> return json.decodeFromJsonElement(ComparisonFilter.serializer(), value)
          "and", "or" -> return CaseCompoundFilter(json.decodeFromJsonElement(CompoundFilter.serializer(), value))
          else -> throw SerializationException("Unknown tag: " + tag + " for io.github.nomisrev.render.test.union.discriminated.recursive.ref.fallback.CompoundFilter.Filters")
        }
      }

      override fun serialize(encoder: Encoder, `value`: Filters) {
        when(value) {
          is ComparisonFilter -> encoder.encodeSerializableValue(ComparisonFilter.serializer(), value)
          is CaseCompoundFilter -> encoder.encodeSerializableValue(CompoundFilter.serializer(), value.value)
        }
      }
    }
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("and")
    And("and"),
    @SerialName("or")
    Or("or"),
    ;
  }
}
