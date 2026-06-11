package io.github.nomisrev.render.test.union.references

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

@Serializable(with = Union.Serializer::class)
public sealed interface Union {
  @Serializable
  @JvmInline
  public value class CasePerson(
    public val `value`: Person,
  ) : Union

  @Serializable
  @JvmInline
  public value class CaseCompany(
    public val `value`: Company,
  ) : Union

  public object Serializer : KSerializer<Union> {
    @OptIn(
      InternalSerializationApi::class,
      ExperimentalSerializationApi::class,
    )
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor("io.github.nomisrev.render.test.union.references.Union", PolymorphicKind.SEALED) {
      element("CasePerson", Person.serializer().descriptor)
      element("CaseCompany", Company.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): Union {
      val value = decoder.decodeSerializableValue(JsonElement.serializer())
      val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
      return json.attemptDeserialize(
        value,
        CasePerson::class to { CasePerson(decodeFromJsonElement(Person.serializer(), it)) },
        CaseCompany::class to { CaseCompany(decodeFromJsonElement(Company.serializer(), it)) },
      )
    }

    override fun serialize(encoder: Encoder, `value`: Union) {
      when(value) {
        is CasePerson -> encoder.encodeSerializableValue(Person.serializer(), value.value)
        is CaseCompany -> encoder.encodeSerializableValue(Company.serializer(), value.value)
      }
    }
  }
}
