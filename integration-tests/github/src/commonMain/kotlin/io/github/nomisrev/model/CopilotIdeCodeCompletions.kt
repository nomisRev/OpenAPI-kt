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
@Serializable(with = CopilotIdeCodeCompletions.Serializer::class)
data class CopilotIdeCodeCompletions(
    @SerialName("total_engaged_users") val totalEngagedUsers: Long? = null,
    val languages: List<Languages>? = null,
    val editors: List<Editors>? = null,
    val additional: JsonObject? = null,
) {
    @Serializable
    data class Languages(val name: String? = null, @SerialName("total_engaged_users") val totalEngagedUsers: Long? = null)

    @OptIn(ExperimentalSerializationApi::class)
    @KeepGeneratedSerializer
    @Serializable(with = Editors.Serializer::class)
    data class Editors(
        val name: String? = null,
        @SerialName("total_engaged_users") val totalEngagedUsers: Long? = null,
        val models: List<Models>? = null,
        val additional: JsonObject? = null,
    ) {
        @Serializable
        data class Models(
            val name: String? = null,
            @SerialName("is_custom_model") val isCustomModel: Boolean? = null,
            @SerialName("custom_model_training_date") val customModelTrainingDate: String? = null,
            @SerialName("total_engaged_users") val totalEngagedUsers: Long? = null,
            val languages: List<Languages>? = null,
        ) {
            @Serializable
            data class Languages(
                val name: String? = null,
                @SerialName("total_engaged_users") val totalEngagedUsers: Long? = null,
                @SerialName("total_code_suggestions") val totalCodeSuggestions: Long? = null,
                @SerialName("total_code_acceptances") val totalCodeAcceptances: Long? = null,
                @SerialName("total_code_lines_suggested") val totalCodeLinesSuggested: Long? = null,
                @SerialName("total_code_lines_accepted") val totalCodeLinesAccepted: Long? = null,
            )
        }

        object Serializer : KSerializer<Editors> {
            override val descriptor: SerialDescriptor = generatedSerializer().descriptor

            override fun serialize(encoder: Encoder, value: Editors) {
                val json = (encoder as JsonEncoder).json
                return encoder.encodeSerializableValue(
                    JsonObject.serializer(),
                    buildJsonObject {
                        put("name", json.encodeToJsonElement(String.serializer().nullable, value.name))
                        put("totalEngagedUsers", json.encodeToJsonElement(Long.serializer().nullable, value.totalEngagedUsers))
                        put("models", json.encodeToJsonElement(ListSerializer(Editors.Models.serializer()).nullable, value.models))
                        putAll(value.additional)
                    })
            }

            override fun deserialize(decoder: Decoder): Editors {
                val json = (decoder as JsonDecoder).json
                val element = decoder.decodeSerializableValue(JsonObject.serializer())
                val names = setOf("name", "total_engaged_users", "models")
                require(element.keys.containsAll(names)) { "Missing required properties: ${names - element.keys}" }
                return Editors(
                    name = if(element.containsKey("name")) json.decodeFromJsonElement(String.serializer().nullable, element["name"]!!) else null,
                    totalEngagedUsers = if(element.containsKey("totalEngagedUsers")) json.decodeFromJsonElement(Long.serializer().nullable, element["totalEngagedUsers"]!!) else null,
                    models = if(element.containsKey("models")) json.decodeFromJsonElement(ListSerializer(Editors.Models.serializer()).nullable, element["models"]!!) else null,
                    additional = JsonObject(element - names).ifEmpty { null }
                )
            }
        }
    }

    object Serializer : KSerializer<CopilotIdeCodeCompletions> {
        override val descriptor: SerialDescriptor = generatedSerializer().descriptor

        override fun serialize(encoder: Encoder, value: CopilotIdeCodeCompletions) {
            val json = (encoder as JsonEncoder).json
            return encoder.encodeSerializableValue(
                JsonObject.serializer(),
                buildJsonObject {
                    put("totalEngagedUsers", json.encodeToJsonElement(Long.serializer().nullable, value.totalEngagedUsers))
                    put("languages", json.encodeToJsonElement(ListSerializer(Languages.serializer()).nullable, value.languages))
                    put("editors", json.encodeToJsonElement(ListSerializer(Editors.serializer()).nullable, value.editors))
                    putAll(value.additional)
                })
        }

        override fun deserialize(decoder: Decoder): CopilotIdeCodeCompletions {
            val json = (decoder as JsonDecoder).json
            val element = decoder.decodeSerializableValue(JsonObject.serializer())
            val names = setOf("total_engaged_users", "languages", "editors")
            require(element.keys.containsAll(names)) { "Missing required properties: ${names - element.keys}" }
            return CopilotIdeCodeCompletions(
                totalEngagedUsers = if(element.containsKey("totalEngagedUsers")) json.decodeFromJsonElement(Long.serializer().nullable, element["totalEngagedUsers"]!!) else null,
                languages = if(element.containsKey("languages")) json.decodeFromJsonElement(ListSerializer(Languages.serializer()).nullable, element["languages"]!!) else null,
                editors = if(element.containsKey("editors")) json.decodeFromJsonElement(ListSerializer(Editors.serializer()).nullable, element["editors"]!!) else null,
                additional = JsonObject(element - names).ifEmpty { null }
            )
        }
    }
}
