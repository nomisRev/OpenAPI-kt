package io.github.model

import kotlin.Boolean
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.EncodeDefault
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
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val totalEngagedUsers: Long? = null,
  @EncodeDefault(EncodeDefault.Mode.NEVER)
  public val editors: List<Editors>? = null,
  public val additional: JsonObject? = null,
) {
  /**
   * Copilot Chat metrics, for active editors.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @Serializable
  public data class Editors(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val name: String? = null,
    @SerialName("total_engaged_users")
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val totalEngagedUsers: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    public val models: List<Models>? = null,
  ) {
    @OptIn(ExperimentalSerializationApi::class)
    @Serializable
    public data class Models(
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val name: String? = null,
      @SerialName("is_custom_model")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val isCustomModel: Boolean? = null,
      @SerialName("custom_model_training_date")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val customModelTrainingDate: Optional<String> = Optional.Absent,
      @SerialName("total_engaged_users")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val totalEngagedUsers: Long? = null,
      @SerialName("total_chats")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val totalChats: Long? = null,
      @SerialName("total_chat_insertion_events")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val totalChatInsertionEvents: Long? = null,
      @SerialName("total_chat_copy_events")
      @EncodeDefault(EncodeDefault.Mode.NEVER)
      public val totalChatCopyEvents: Long? = null,
    )
  }

  public object Serializer : KSerializer<CopilotIdeChat> {
    override val descriptor: SerialDescriptor = generatedSerializer().descriptor

    override fun serialize(encoder: Encoder, `value`: CopilotIdeChat) {
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
