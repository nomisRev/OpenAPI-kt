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
import kotlinx.serialization.builtins.nullable

@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = CopilotOrganizationDetails.Serializer::class)
data class CopilotOrganizationDetails(
    @SerialName("seat_breakdown") val seatBreakdown: CopilotOrganizationSeatBreakdown,
    @SerialName("public_code_suggestions") val publicCodeSuggestions: PublicCodeSuggestions,
    @SerialName("ide_chat") val ideChat: IdeChat? = null,
    @SerialName("platform_chat") val platformChat: PlatformChat? = null,
    val cli: Cli? = null,
    @SerialName("seat_management_setting") val seatManagementSetting: SeatManagementSetting,
    @SerialName("plan_type") val planType: PlanType? = null,
    val additional: JsonObject? = null,
) {
    @Serializable
    enum class PublicCodeSuggestions {
        @SerialName("allow") Allow, @SerialName("block") Block, @SerialName("unconfigured") Unconfigured;
    }

    @Serializable
    enum class IdeChat {
        @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("unconfigured") Unconfigured;
    }

    @Serializable
    enum class PlatformChat {
        @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("unconfigured") Unconfigured;
    }

    @Serializable
    enum class Cli {
        @SerialName("enabled") Enabled, @SerialName("disabled") Disabled, @SerialName("unconfigured") Unconfigured;
    }

    @Serializable
    enum class SeatManagementSetting {
        @SerialName("assign_all")
        AssignAll,
        @SerialName("assign_selected")
        AssignSelected,
        @SerialName("disabled")
        Disabled,
        @SerialName("unconfigured")
        Unconfigured;
    }

    @Serializable
    enum class PlanType {
        @SerialName("business") Business, @SerialName("enterprise") Enterprise;
    }

    object Serializer : KSerializer<CopilotOrganizationDetails> {
        override val descriptor: SerialDescriptor = generatedSerializer().descriptor

        override fun serialize(encoder: Encoder, value: CopilotOrganizationDetails) {
            val json = (encoder as JsonEncoder).json
            return encoder.encodeSerializableValue(
                JsonObject.serializer(),
                buildJsonObject {
                    put("seatBreakdown", json.encodeToJsonElement(CopilotOrganizationSeatBreakdown.serializer(), value.seatBreakdown))
                    put("publicCodeSuggestions", json.encodeToJsonElement(PublicCodeSuggestions.serializer(), value.publicCodeSuggestions))
                    put("ideChat", json.encodeToJsonElement(IdeChat.serializer().nullable, value.ideChat))
                    put("platformChat", json.encodeToJsonElement(PlatformChat.serializer().nullable, value.platformChat))
                    put("cli", json.encodeToJsonElement(Cli.serializer().nullable, value.cli))
                    put("seatManagementSetting", json.encodeToJsonElement(SeatManagementSetting.serializer(), value.seatManagementSetting))
                    put("planType", json.encodeToJsonElement(PlanType.serializer().nullable, value.planType))
                    putAll(value.additional)
                })
        }

        override fun deserialize(decoder: Decoder): CopilotOrganizationDetails {
            val json = (decoder as JsonDecoder).json
            val element = decoder.decodeSerializableValue(JsonObject.serializer())
            val names = setOf("seat_breakdown", "public_code_suggestions", "ide_chat", "platform_chat", "cli", "seat_management_setting", "plan_type")
            require(element.keys.containsAll(names)) { "Missing required properties: ${names - element.keys}" }
            return CopilotOrganizationDetails(
                seatBreakdown = json.decodeFromJsonElement(CopilotOrganizationSeatBreakdown.serializer(), element["seatBreakdown"]!!),
                publicCodeSuggestions = json.decodeFromJsonElement(PublicCodeSuggestions.serializer(), element["publicCodeSuggestions"]!!),
                ideChat = if(element.containsKey("ideChat")) json.decodeFromJsonElement(IdeChat.serializer().nullable, element["ideChat"]!!) else null,
                platformChat = if(element.containsKey("platformChat")) json.decodeFromJsonElement(PlatformChat.serializer().nullable, element["platformChat"]!!) else null,
                cli = if(element.containsKey("cli")) json.decodeFromJsonElement(Cli.serializer().nullable, element["cli"]!!) else null,
                seatManagementSetting = json.decodeFromJsonElement(SeatManagementSetting.serializer(), element["seatManagementSetting"]!!),
                planType = if(element.containsKey("planType")) json.decodeFromJsonElement(PlanType.serializer().nullable, element["planType"]!!) else null,
                additional = JsonObject(element - names).ifEmpty { null }
            )
        }
    }
}
