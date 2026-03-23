package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
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
 * Usage metrics for Copilot Chat in the IDE.
 */
@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = CopilotIdeChat.Serializer::class)
public data class CopilotIdeChat(
  @SerialName("total_engaged_users")
  public val totalEngagedUsers: Long? = null,
  public val editors: List<Editors>? = null,
  public val additional: JsonObject? = null,
) {
  /**
   * Copilot Chat metrics, for active editors.
   */
  @Serializable
  public data class Editors(
    public val name: String? = null,
    @SerialName("total_engaged_users")
    public val totalEngagedUsers: Long? = null,
    public val models: List<Models>? = null,
  ) {
    @Serializable
    public data class Models(
      public val name: String? = null,
      @SerialName("is_custom_model")
      public val isCustomModel: Boolean? = null,
      @SerialName("custom_model_training_date")
      public val customModelTrainingDate: String? = null,
      @SerialName("total_engaged_users")
      public val totalEngagedUsers: Long? = null,
      @SerialName("total_chats")
      public val totalChats: Long? = null,
      @SerialName("total_chat_insertion_events")
      public val totalChatInsertionEvents: Long? = null,
      @SerialName("total_chat_copy_events")
      public val totalChatCopyEvents: Long? = null,
    )
  }

  public object Serializer : KSerializer<CopilotIdeChat> {
    override val descriptor: SerialDescriptor = generatedSerializer().descriptor

    override fun serialize(encoder: Encoder, `value`: CopilotIdeChat) {
      val json = (encoder as JsonEncoder).json
      val known = json.encodeToJsonElement(generatedSerializer(), value.copy(additional = null)) as JsonObject
      val content = mutableMapOf<String, JsonElement>()
      known.forEach { (key, jsonElement) ->
        if (key != "additional") {
          content[key] = jsonElement
        }
      }
      value.additional?.forEach { (key, jsonElement) ->
        content[key] = jsonElement
      }
      encoder.encodeSerializableValue(JsonObject.serializer(), JsonObject(content))
    }

    override fun deserialize(decoder: Decoder): CopilotIdeChat {
      val json = (decoder as JsonDecoder).json
      val element = decoder.decodeSerializableValue(JsonObject.serializer())
      val knownNames = setOf("total_engaged_users", "editors")
      val known = json.decodeFromJsonElement(generatedSerializer(), JsonObject(element.filterKeys { it in knownNames }))
      val additional = JsonObject(element - knownNames).ifEmpty { null }
      return known.copy(additional = additional)
    }
  }
}
