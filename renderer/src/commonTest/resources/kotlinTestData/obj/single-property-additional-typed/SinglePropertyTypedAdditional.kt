package obj.single.property.additional.typed.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.builtins.serializer

@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = SinglePropertyTypedAdditional.Serializer::class)
data class SinglePropertyTypedAdditional(val name: String, val additional: Map<String, Int>? = null) {

    object Serializer : KSerializer<SinglePropertyTypedAdditional> {
        override val descriptor: SerialDescriptor = generatedSerializer().descriptor

        override fun serialize(encoder: Encoder, value: SinglePropertyTypedAdditional) {
            val json = (encoder as JsonEncoder).json
            return encoder.encodeSerializableValue(
                JsonObject.serializer(),
                buildJsonObject {
                    put("name", json.encodeToJsonElement(String.serializer(), value.name))
                    value.additional?.forEach { (key, value) ->
                        put(key, json.encodeToJsonElement(Int.serializer(), value))
                    }
                })
        }

        override fun deserialize(decoder: Decoder): SinglePropertyTypedAdditional {
            val json = (decoder as JsonDecoder).json
            val element = decoder.decodeSerializableValue(JsonObject.serializer())
            val names = setOf("name")
            require(element.keys.containsAll(names)) { "Missing required properties: ${names - element.keys}" }
            return SinglePropertyTypedAdditional(
                name = json.decodeFromJsonElement(String.serializer(), element["name"]!!),
                additional = (element - names)
                    .mapValues { (_, value) -> json.decodeFromJsonElement(Int.serializer(), value) }
            )
        }
    }
}
