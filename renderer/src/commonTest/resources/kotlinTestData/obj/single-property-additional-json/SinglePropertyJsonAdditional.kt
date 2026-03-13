package obj.single.property.additional.json.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.builtins.serializer

@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = SinglePropertyJsonAdditional.Serializer::class)
data class SinglePropertyJsonAdditional(val name: String, val additional: JsonObject? = null) {

    object Serializer : KSerializer<SinglePropertyJsonAdditional> {
        override val descriptor: SerialDescriptor = generatedSerializer().descriptor

        override fun serialize(encoder: Encoder, value: SinglePropertyJsonAdditional) {
            val json = (encoder as JsonEncoder).json
            return encoder.encodeSerializableValue(
                JsonObject.serializer(),
                buildJsonObject {
                    put("name", json.encodeToJsonElement(String.serializer(), value.name))
                    putAll(value.additional)
                })
        }

        override fun deserialize(decoder: Decoder): SinglePropertyJsonAdditional {
            val json = (decoder as JsonDecoder).json
            val element = decoder.decodeSerializableValue(JsonObject.serializer())
            val names = setOf("name")
            require(element.keys.containsAll(names)) { "Missing required properties: ${names - element.keys}" }
            return SinglePropertyJsonAdditional(
                name = json.decodeFromJsonElement(String.serializer(), element["name"]!!),
                additional = JsonObject(element - names).ifEmpty { null }
            )
        }
    }
}
