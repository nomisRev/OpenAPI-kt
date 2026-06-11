package io.github.model

import kotlin.OptIn
import kotlin.String
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
 * Information about the seat breakdown and policies set for an organization with a Copilot Business or Copilot Enterprise subscription.
 */
@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = CopilotOrganizationDetails.Serializer::class)
public data class CopilotOrganizationDetails(
  @SerialName("seat_breakdown")
  public val seatBreakdown: CopilotOrganizationSeatBreakdown,
  @SerialName("public_code_suggestions")
  public val publicCodeSuggestions: PublicCodeSuggestions,
  @SerialName("ide_chat")
  public val ideChat: IdeChat? = null,
  @SerialName("platform_chat")
  public val platformChat: PlatformChat? = null,
  public val cli: Cli? = null,
  @SerialName("seat_management_setting")
  public val seatManagementSetting: SeatManagementSetting,
  @SerialName("plan_type")
  public val planType: PlanType? = null,
  public val additional: JsonObject? = null,
) {
  @Serializable
  public enum class Cli(
    public val `value`: String,
  ) {
    @SerialName("enabled")
    Enabled("enabled"),
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("unconfigured")
    Unconfigured("unconfigured"),
    ;
  }

  @Serializable
  public enum class IdeChat(
    public val `value`: String,
  ) {
    @SerialName("enabled")
    Enabled("enabled"),
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("unconfigured")
    Unconfigured("unconfigured"),
    ;
  }

  @Serializable
  public enum class PlanType(
    public val `value`: String,
  ) {
    @SerialName("business")
    Business("business"),
    @SerialName("enterprise")
    Enterprise("enterprise"),
    ;
  }

  @Serializable
  public enum class PlatformChat(
    public val `value`: String,
  ) {
    @SerialName("enabled")
    Enabled("enabled"),
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("unconfigured")
    Unconfigured("unconfigured"),
    ;
  }

  @Serializable
  public enum class PublicCodeSuggestions(
    public val `value`: String,
  ) {
    @SerialName("allow")
    Allow("allow"),
    @SerialName("block")
    Block("block"),
    @SerialName("unconfigured")
    Unconfigured("unconfigured"),
    ;
  }

  @Serializable
  public enum class SeatManagementSetting(
    public val `value`: String,
  ) {
    @SerialName("assign_all")
    AssignAll("assign_all"),
    @SerialName("assign_selected")
    AssignSelected("assign_selected"),
    @SerialName("disabled")
    Disabled("disabled"),
    @SerialName("unconfigured")
    Unconfigured("unconfigured"),
    ;
  }

  public object Serializer : KSerializer<CopilotOrganizationDetails> {
    override val descriptor: SerialDescriptor = generatedSerializer().descriptor

    override fun serialize(encoder: Encoder, `value`: CopilotOrganizationDetails) {
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

    override fun deserialize(decoder: Decoder): CopilotOrganizationDetails {
      val json = (decoder as JsonDecoder).json
      val element = decoder.decodeSerializableValue(JsonObject.serializer())
      val knownNames = setOf("seat_breakdown", "public_code_suggestions", "ide_chat", "platform_chat", "cli", "seat_management_setting", "plan_type")
      val known = json.decodeFromJsonElement(generatedSerializer(), JsonObject(element.filterKeys { it in knownNames }))
      val additional = JsonObject(element - knownNames).ifEmpty { null }
      return known.copy(additional = additional)
    }
  }
}
