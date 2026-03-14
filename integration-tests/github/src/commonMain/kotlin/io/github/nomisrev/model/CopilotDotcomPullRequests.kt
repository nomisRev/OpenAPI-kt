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
@Serializable(with = CopilotDotcomPullRequests.Serializer::class)
data class CopilotDotcomPullRequests(
    @SerialName("total_engaged_users") val totalEngagedUsers: Long? = null,
    val repositories: List<Repositories>? = null,
    val additional: JsonObject? = null,
) {
    @Serializable
    data class Repositories(
        val name: String? = null,
        @SerialName("total_engaged_users") val totalEngagedUsers: Long? = null,
        val models: List<Models>? = null,
    ) {
        @Serializable
        data class Models(
            val name: String? = null,
            @SerialName("is_custom_model") val isCustomModel: Boolean? = null,
            @SerialName("custom_model_training_date") val customModelTrainingDate: String? = null,
            @SerialName("total_pr_summaries_created") val totalPrSummariesCreated: Long? = null,
            @SerialName("total_engaged_users") val totalEngagedUsers: Long? = null,
        )
    }

    object Serializer : KSerializer<CopilotDotcomPullRequests> {
        override val descriptor: SerialDescriptor = generatedSerializer().descriptor

        override fun serialize(encoder: Encoder, value: CopilotDotcomPullRequests) {
            val json = (encoder as JsonEncoder).json
            return encoder.encodeSerializableValue(
                JsonObject.serializer(),
                buildJsonObject {
                    put("totalEngagedUsers", json.encodeToJsonElement(Long.serializer().nullable, value.totalEngagedUsers))
                    put("repositories", json.encodeToJsonElement(ListSerializer(Repositories.serializer()).nullable, value.repositories))
                    putAll(value.additional)
                })
        }

        override fun deserialize(decoder: Decoder): CopilotDotcomPullRequests {
            val json = (decoder as JsonDecoder).json
            val element = decoder.decodeSerializableValue(JsonObject.serializer())
            val names = setOf("total_engaged_users", "repositories")
            require(element.keys.containsAll(names)) { "Missing required properties: ${names - element.keys}" }
            return CopilotDotcomPullRequests(
                totalEngagedUsers = if(element.containsKey("totalEngagedUsers")) json.decodeFromJsonElement(Long.serializer().nullable, element["totalEngagedUsers"]!!) else null,
                repositories = if(element.containsKey("repositories")) json.decodeFromJsonElement(ListSerializer(Repositories.serializer()).nullable, element["repositories"]!!) else null,
                additional = JsonObject(element - names).ifEmpty { null }
            )
        }
    }
}
