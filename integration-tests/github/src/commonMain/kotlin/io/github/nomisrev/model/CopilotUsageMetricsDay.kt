package io.github.nomisrev.model

import kotlinx.datetime.LocalDate
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

@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = CopilotUsageMetricsDay.Serializer::class)
data class CopilotUsageMetricsDay(
    val date: LocalDate,
    @SerialName("total_active_users") val totalActiveUsers: Long? = null,
    @SerialName("total_engaged_users") val totalEngagedUsers: Long? = null,
    @SerialName("copilot_ide_code_completions") val copilotIdeCodeCompletions: CopilotIdeCodeCompletions? = null,
    @SerialName("copilot_ide_chat") val copilotIdeChat: CopilotIdeChat? = null,
    @SerialName("copilot_dotcom_chat") val copilotDotcomChat: CopilotDotcomChat? = null,
    @SerialName("copilot_dotcom_pull_requests") val copilotDotcomPullRequests: CopilotDotcomPullRequests? = null,
    val additional: JsonObject? = null,
) {

    object Serializer : KSerializer<CopilotUsageMetricsDay> {
        override val descriptor: SerialDescriptor = generatedSerializer().descriptor

        override fun serialize(encoder: Encoder, value: CopilotUsageMetricsDay) {
            val json = (encoder as JsonEncoder).json
            return encoder.encodeSerializableValue(
                JsonObject.serializer(),
                buildJsonObject {
                    put("date", json.encodeToJsonElement(LocalDate.serializer(), value.date))
                    put("totalActiveUsers", json.encodeToJsonElement(Long.serializer().nullable, value.totalActiveUsers))
                    put("totalEngagedUsers", json.encodeToJsonElement(Long.serializer().nullable, value.totalEngagedUsers))
                    put("copilotIdeCodeCompletions", json.encodeToJsonElement(CopilotIdeCodeCompletions.serializer().nullable, value.copilotIdeCodeCompletions))
                    put("copilotIdeChat", json.encodeToJsonElement(CopilotIdeChat.serializer().nullable, value.copilotIdeChat))
                    put("copilotDotcomChat", json.encodeToJsonElement(CopilotDotcomChat.serializer().nullable, value.copilotDotcomChat))
                    put("copilotDotcomPullRequests", json.encodeToJsonElement(CopilotDotcomPullRequests.serializer().nullable, value.copilotDotcomPullRequests))
                    putAll(value.additional)
                })
        }

        override fun deserialize(decoder: Decoder): CopilotUsageMetricsDay {
            val json = (decoder as JsonDecoder).json
            val element = decoder.decodeSerializableValue(JsonObject.serializer())
            val names = setOf("date", "total_active_users", "total_engaged_users", "copilot_ide_code_completions", "copilot_ide_chat", "copilot_dotcom_chat", "copilot_dotcom_pull_requests")
            require(element.keys.containsAll(names)) { "Missing required properties: ${names - element.keys}" }
            return CopilotUsageMetricsDay(
                date = json.decodeFromJsonElement(LocalDate.serializer(), element["date"]!!),
                totalActiveUsers = if(element.containsKey("totalActiveUsers")) json.decodeFromJsonElement(Long.serializer().nullable, element["totalActiveUsers"]!!) else null,
                totalEngagedUsers = if(element.containsKey("totalEngagedUsers")) json.decodeFromJsonElement(Long.serializer().nullable, element["totalEngagedUsers"]!!) else null,
                copilotIdeCodeCompletions = if(element.containsKey("copilotIdeCodeCompletions")) json.decodeFromJsonElement(CopilotIdeCodeCompletions.serializer().nullable, element["copilotIdeCodeCompletions"]!!) else null,
                copilotIdeChat = if(element.containsKey("copilotIdeChat")) json.decodeFromJsonElement(CopilotIdeChat.serializer().nullable, element["copilotIdeChat"]!!) else null,
                copilotDotcomChat = if(element.containsKey("copilotDotcomChat")) json.decodeFromJsonElement(CopilotDotcomChat.serializer().nullable, element["copilotDotcomChat"]!!) else null,
                copilotDotcomPullRequests = if(element.containsKey("copilotDotcomPullRequests")) json.decodeFromJsonElement(CopilotDotcomPullRequests.serializer().nullable, element["copilotDotcomPullRequests"]!!) else null,
                additional = JsonObject(element - names).ifEmpty { null }
            )
        }
    }
}
