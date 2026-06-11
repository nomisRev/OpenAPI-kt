package io.github.model

import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlinx.datetime.LocalDate
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject

/**
 * Copilot usage metrics for a given day.
 */
@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = CopilotUsageMetricsDay.Serializer::class)
public data class CopilotUsageMetricsDay(
  public val date: LocalDate,
  @SerialName("total_active_users")
  public val totalActiveUsers: Long? = null,
  @SerialName("total_engaged_users")
  public val totalEngagedUsers: Long? = null,
  @SerialName("copilot_ide_code_completions")
  public val copilotIdeCodeCompletions: CopilotIdeCodeCompletions? = null,
  @SerialName("copilot_ide_chat")
  public val copilotIdeChat: CopilotIdeChat? = null,
  @SerialName("copilot_dotcom_chat")
  public val copilotDotcomChat: CopilotDotcomChat? = null,
  @SerialName("copilot_dotcom_pull_requests")
  public val copilotDotcomPullRequests: CopilotDotcomPullRequests? = null,
  public val additional: JsonObject? = null,
) {
  public object Serializer : KSerializer<CopilotUsageMetricsDay> {
    override val descriptor: SerialDescriptor = generatedSerializer().descriptor

    override fun serialize(encoder: Encoder, `value`: CopilotUsageMetricsDay) {
      val json = (encoder as JsonEncoder).json
      val known = json.encodeToJsonElement(generatedSerializer(), value.copy(additional = null)) as JsonObject
      val content = mutableMapOf<String, JsonElement>()
      known.forEach {
        if (it.key != "additional") {
          content[it.key] = it.value
        }
      }
      value.additional?.forEach {
        content[it.key] = it.value
      }
      encoder.encodeSerializableValue(JsonObject.serializer(), JsonObject(content))
    }

    override fun deserialize(decoder: Decoder): CopilotUsageMetricsDay {
      val json = (decoder as JsonDecoder).json
      val element = decoder.decodeSerializableValue(JsonObject.serializer())
      val knownNames = setOf("date", "total_active_users", "total_engaged_users", "copilot_ide_code_completions", "copilot_ide_chat", "copilot_dotcom_chat", "copilot_dotcom_pull_requests")
      val known = json.decodeFromJsonElement(generatedSerializer(), JsonObject(element.filterKeys { it in knownNames }))
      val additional = JsonObject(element - knownNames).ifEmpty { null }
      return known.copy(additional = additional)
    }
  }
}
