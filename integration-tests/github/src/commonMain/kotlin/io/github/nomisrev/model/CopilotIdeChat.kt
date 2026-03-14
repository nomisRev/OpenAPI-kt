package io.github.nomisrev.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.SerialName
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.ListSerializer

@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = CopilotIdeChat.Serializer::class)
data class CopilotIdeChat(
    @SerialName("total_engaged_users") val totalEngagedUsers: Long? = null,
    val editors: List<Editors>? = null,
    val additional: JsonObject? = null,
) {
    @Serializable
    data class Editors(
        val name: String? = null,
        @SerialName("total_engaged_users") val totalEngagedUsers: Long? = null,
        val models: List<Models>? = null,
    ) {
        @Serializable
        data class Models(
            val name: String? = null,
            @SerialName("is_custom_model") val isCustomModel: Boolean? = null,
            @SerialName("custom_model_training_date") val customModelTrainingDate: String? = null,
            @SerialName("total_engaged_users") val totalEngagedUsers: Long? = null,
            @SerialName("total_chats") val totalChats: Long? = null,
            @SerialName("total_chat_insertion_events") val totalChatInsertionEvents: Long? = null,
            @SerialName("total_chat_copy_events") val totalChatCopyEvents: Long? = null,
        )
    }

    object Serializer : KSerializer<CopilotIdeChat> {
        override val descriptor: SerialDescriptor = generatedSerializer().descriptor

        override fun serialize(encoder: Encoder, value: CopilotIdeChat) {
            val json = (encoder as JsonEncoder).json
            return encoder.encodeSerializableValue(
                JsonObject.serializer(),
                buildJsonObject {
                    put("totalEngagedUsers", json.encodeToJsonElement(Long.serializer().nullable, value.totalEngagedUsers))
                    put("editors", json.encodeToJsonElement(ListSerializer(Editors.serializer()).nullable, value.editors))
                    putAll(value.additional)
                })
        }

        override fun deserialize(decoder: Decoder): CopilotIdeChat {
            val json = (decoder as JsonDecoder).json
            val element = decoder.decodeSerializableValue(JsonObject.serializer())
            val names = setOf("total_engaged_users", "editors")
            require(element.keys.containsAll(names)) { "Missing required properties: ${names - element.keys}" }
            return CopilotIdeChat(
                totalEngagedUsers = if(element.containsKey("totalEngagedUsers")) json.decodeFromJsonElement(Long.serializer().nullable, element["totalEngagedUsers"]!!) else null,
                editors = if(element.containsKey("editors")) json.decodeFromJsonElement(ListSerializer(Editors.serializer()).nullable, element["editors"]!!) else null,
                additional = JsonObject(element - names).ifEmpty { null }
            )
        }
    }
}
