package union.references.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder

@Serializable(with = Union.Serializer::class)
sealed interface Union {
    @Serializable
    @JvmInline
    value class CasePerson(val value: Person) : Union

    @Serializable
    @JvmInline
    value class CaseCompany(val value: Company) : Union

    object Serializer : KSerializer<Union> {
        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("union.references.model.Union", PolymorphicKind.SEALED) {
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

        override fun serialize(encoder: Encoder, value: Union) = when(value) {
            is CasePerson -> encoder.encodeSerializableValue(Person.serializer(), value.value)
            is CaseCompany -> encoder.encodeSerializableValue(Company.serializer(), value.value)
        }
    }
}
