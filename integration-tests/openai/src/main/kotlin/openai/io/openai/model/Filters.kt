package io.openai.model

import kotlin.OptIn
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

@Serializable(with = Filters.Serializer::class)
public sealed interface Filters {
  @Serializable
  @JvmInline
  public value class CaseComparisonFilter(
    public val `value`: ComparisonFilter,
  ) : Filters

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
        buildSerialDescriptor("io.openai.model.Filters", PolymorphicKind.SEALED) {
      element("CaseComparisonFilter", ComparisonFilter.serializer().descriptor)
      element("CaseCompoundFilter", CompoundFilter.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): Filters {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CaseComparisonFilter::class to { CaseComparisonFilter(decodeFromJsonElement(ComparisonFilter.serializer(), it)) },
        CaseCompoundFilter::class to { CaseCompoundFilter(decodeFromJsonElement(CompoundFilter.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: Filters) {
      when(value) {
        is CaseComparisonFilter -> encoder.encodeSerializableValue(ComparisonFilter.serializer(), value.value)
        is CaseCompoundFilter -> encoder.encodeSerializableValue(CompoundFilter.serializer(), value.value)
      }
    }
  }
}
