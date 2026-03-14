package union.nested.discriminated.obj.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonDecoder

@Serializable(with = Union.Serializer::class)
sealed interface Union {
    @OptIn(ExperimentalSerializationApi::class)
    @JsonClassDiscriminator("kind")
    @Serializable
    sealed interface Auth : Union {
        val kind: String

        @SerialName("password")
        @Serializable
        data class Password(override val kind: String, val password: String) : Auth

        @SerialName("token")
        @Serializable
        data class Token(override val kind: String, val token: String) : Auth
    }

    @Serializable
    @JvmInline
    value class CaseInt(val value: Int) : Union

    object Serializer : KSerializer<Union> {
        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("union.nested.discriminated.obj.model.Union", PolymorphicKind.SEALED) {
                element("CaseAuth", Auth.serializer().descriptor)
                element("CaseInt", Int.serializer().descriptor)
            }

        override fun deserialize(decoder: Decoder): Union {
            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            val json =
                requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            return json.attemptDeserialize(
                value,
                Auth::class to { decodeFromJsonElement(Auth.serializer(), it) },
                CaseInt::class to { CaseInt(decodeFromJsonElement(Int.serializer(), it)) },
            )
        }

        override fun serialize(encoder: Encoder, value: Union) = when (value) {
            is Auth -> encoder.encodeSerializableValue(Auth.serializer(), value)
            is CaseInt -> encoder.encodeSerializableValue(Int.serializer(), value.value)
        }
    }
}
