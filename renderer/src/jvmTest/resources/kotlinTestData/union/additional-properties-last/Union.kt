package union.additional.properties.last.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.json.JsonElement

@Serializable(with = Union.Serializer::class)
sealed interface Union {
    @Serializable
    @JvmInline
    value class Strict(val name: String) : Union

    @OptIn(ExperimentalSerializationApi::class)
    @KeepGeneratedSerializer
    @Serializable(with = WithAdditional.Serializer::class)
    data class WithAdditional(val name: String, val additional: JsonObject? = null) : Union {

        object Serializer : KSerializer<WithAdditional> {
            override val descriptor: SerialDescriptor = generatedSerializer().descriptor

            override fun serialize(encoder: Encoder, value: WithAdditional) {
                val json = (encoder as JsonEncoder).json
                return encoder.encodeSerializableValue(
                    JsonObject.serializer(),
                    buildJsonObject {
                        put("name", json.encodeToJsonElement(String.serializer(), value.name))
                        putAll(value.additional)
                    })
            }

            override fun deserialize(decoder: Decoder): WithAdditional {
                val json = (decoder as JsonDecoder).json
                val element = decoder.decodeSerializableValue(JsonObject.serializer())
                val names = setOf("name")
                require(element.keys.containsAll(names)) { "Missing required properties: ${names - element.keys}" }
                return WithAdditional(
                    name = json.decodeFromJsonElement(String.serializer(), element["name"]!!),
                    additional = JsonObject(element - names).ifEmpty { null }
                )
            }
        }
    }

    object Serializer : KSerializer<Union> {
        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
        override val descriptor: SerialDescriptor =
            buildSerialDescriptor("union.additional.properties.last.model.Union", PolymorphicKind.SEALED) {
                element("Strict", Strict.serializer().descriptor)
                element("WithAdditional", WithAdditional.serializer().descriptor)
            }

        override fun deserialize(decoder: Decoder): Union {
            val value = decoder.decodeSerializableValue(JsonElement.serializer())
            val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
            return json.attemptDeserialize(
                value,
                Strict::class to { decodeFromJsonElement(Strict.serializer(), it) },
                WithAdditional::class to { decodeFromJsonElement(WithAdditional.serializer(), it) },
            )
        }

        override fun serialize(encoder: Encoder, value: Union) = when(value) {
            is Strict -> encoder.encodeSerializableValue(Strict.serializer(), value)
            is WithAdditional -> encoder.encodeSerializableValue(WithAdditional.serializer(), value)
        }
    }
}
