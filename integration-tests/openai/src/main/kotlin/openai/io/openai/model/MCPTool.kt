package io.openai.model

import kotlin.Boolean
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * Give the model access to additional tools via remote Model Context Protocol
 * (MCP) servers. [Learn more about MCP](/docs/guides/tools-remote-mcp).
 *
 */
@Serializable
public data class MCPTool(
  public val type: Type,
  @SerialName("server_label")
  public val serverLabel: String,
  @SerialName("server_url")
  public val serverUrl: String? = null,
  @SerialName("connector_id")
  public val connectorId: ConnectorId? = null,
  public val authorization: String? = null,
  @SerialName("server_description")
  public val serverDescription: String? = null,
  public val headers: List<String>? = null,
  @SerialName("allowed_tools")
  public val allowedTools: AllowedTools? = null,
  @SerialName("require_approval")
  public val requireApproval: RequireApproval? = null,
  @SerialName("defer_loading")
  public val deferLoading: Boolean? = null,
) {
  /**
   * List of allowed tool names or a filter object.
   *
   */
  @Serializable(with = AllowedTools.Serializer::class)
  public sealed interface AllowedTools {
    @Serializable
    @JvmInline
    public value class CaseStrings(
      public val `value`: List<String>,
    ) : AllowedTools

    @Serializable
    @JvmInline
    public value class CaseMCPToolFilter(
      public val `value`: MCPToolFilter,
    ) : AllowedTools

    public object Serializer : KSerializer<AllowedTools> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.MCPTool.AllowedTools", PolymorphicKind.SEALED) {
        element("CaseStrings", ListSerializer(String.serializer()).descriptor)
        element("CaseMCPToolFilter", MCPToolFilter.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): AllowedTools {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseStrings::class to { CaseStrings(decodeFromJsonElement(ListSerializer(String.serializer()), it)) },
          CaseMCPToolFilter::class to { CaseMCPToolFilter(decodeFromJsonElement(MCPToolFilter.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: AllowedTools) {
        when(value) {
          is CaseStrings -> encoder.encodeSerializableValue(ListSerializer(String.serializer()), value.value)
          is CaseMCPToolFilter -> encoder.encodeSerializableValue(MCPToolFilter.serializer(), value.value)
        }
      }
    }
  }

  @Serializable
  public enum class ConnectorId(
    public val `value`: String,
  ) {
    @SerialName("connector_dropbox")
    ConnectorDropbox("connector_dropbox"),
    @SerialName("connector_gmail")
    ConnectorGmail("connector_gmail"),
    @SerialName("connector_googlecalendar")
    ConnectorGooglecalendar("connector_googlecalendar"),
    @SerialName("connector_googledrive")
    ConnectorGoogledrive("connector_googledrive"),
    @SerialName("connector_microsoftteams")
    ConnectorMicrosoftteams("connector_microsoftteams"),
    @SerialName("connector_outlookcalendar")
    ConnectorOutlookcalendar("connector_outlookcalendar"),
    @SerialName("connector_outlookemail")
    ConnectorOutlookemail("connector_outlookemail"),
    @SerialName("connector_sharepoint")
    ConnectorSharepoint("connector_sharepoint"),
    ;
  }

  /**
   * Specify which of the MCP server's tools require approval.
   */
  @Serializable(with = RequireApproval.Serializer::class)
  public sealed interface RequireApproval {
    /**
     * Specify which of the MCP server's tools require approval. Can be
     * `always`, `never`, or a filter object associated with tools
     * that require approval.
     *
     */
    @Serializable
    public data class MCPToolApprovalFilter(
      public val always: MCPToolFilter? = null,
      public val never: MCPToolFilter? = null,
    ) : RequireApproval

    @Serializable
    public enum class MCPToolApprovalSetting(
      public val `value`: String,
    ) : RequireApproval {
      @SerialName("always")
      Always("always"),
      @SerialName("never")
      Never("never"),
      ;
    }

    public object Serializer : KSerializer<RequireApproval> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.MCPTool.RequireApproval", PolymorphicKind.SEALED) {
        element("MCPToolApprovalFilter", MCPToolApprovalFilter.serializer().descriptor)
        element("MCPToolApprovalSetting", MCPToolApprovalSetting.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): RequireApproval {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          MCPToolApprovalFilter::class to { decodeFromJsonElement(MCPToolApprovalFilter.serializer(), it) },
          MCPToolApprovalSetting::class to { decodeFromJsonElement(MCPToolApprovalSetting.serializer(), it) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: RequireApproval) {
        when(value) {
          is MCPToolApprovalFilter -> encoder.encodeSerializableValue(MCPToolApprovalFilter.serializer(), value)
          is MCPToolApprovalSetting -> encoder.encodeSerializableValue(MCPToolApprovalSetting.serializer(), value)
        }
      }
    }
  }

  @Serializable
  public enum class Type(
    public val `value`: String,
  ) {
    @SerialName("mcp")
    Mcp("mcp"),
    ;
  }
}
