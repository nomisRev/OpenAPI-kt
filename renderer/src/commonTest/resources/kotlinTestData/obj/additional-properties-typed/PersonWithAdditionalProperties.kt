package obj.additional.properties.typed.model

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
import kotlinx.serialization.builtins.nullable

@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = PersonWithAdditionalProperties.Serializer::class)
data class PersonWithAdditionalProperties(
    val name: String,
    val age: Int?,
    val additional: Map<String, NestedClass>? = null,
) {
    @Serializable
    data class NestedClass(val config1: String, val config2: Long)

    object Serializer : KSerializer<PersonWithAdditionalProperties> {
        override val descriptor: SerialDescriptor = generatedSerializer().descriptor

        override fun serialize(encoder: Encoder, value: PersonWithAdditionalProperties) {
            val json = (encoder as JsonEncoder).json
            return encoder.encodeSerializableValue(
                JsonObject.serializer(),
                buildJsonObject {
                    put("name", json.encodeToJsonElement(String.serializer(), value.name))
                    put("age", json.encodeToJsonElement(Int.serializer().nullable, value.age))
                    value.additional?.forEach { (key, value) ->
                        put(key, json.encodeToJsonElement(NestedClass.serializer(), value))
                    }
                })
        }

        override fun deserialize(decoder: Decoder): PersonWithAdditionalProperties {
            val json = (decoder as JsonDecoder).json
            val element = decoder.decodeSerializableValue(JsonObject.serializer())
            val names = setOf("name", "age")
            require(element.keys.containsAll(names)) { "Missing required properties: ${names - element.keys}" }
            return PersonWithAdditionalProperties(
                name = json.decodeFromJsonElement(String.serializer(), element["name"]!!),
                age = json.decodeFromJsonElement(Int.serializer().nullable, element["age"]!!),
                additional = (element - names)
                    .mapValues { (_, value) -> json.decodeFromJsonElement(NestedClass.serializer(), value) }
            )
        }
    }
}
