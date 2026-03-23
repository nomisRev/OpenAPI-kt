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
 * Usage metrics for Copilot editor code completions in the IDE.
 */
@OptIn(ExperimentalSerializationApi::class)
@KeepGeneratedSerializer
@Serializable(with = CopilotIdeCodeCompletions.Serializer::class)
public data class CopilotIdeCodeCompletions(
  @SerialName("total_engaged_users")
  public val totalEngagedUsers: Long? = null,
  public val languages: List<Languages>? = null,
  public val editors: List<Editors>? = null,
  public val additional: JsonObject? = null,
) {
  /**
   * Copilot code completion metrics for active editors.
   */
  @OptIn(ExperimentalSerializationApi::class)
  @KeepGeneratedSerializer
  @Serializable(with = Editors.Serializer::class)
  public data class Editors(
    public val name: String? = null,
    @SerialName("total_engaged_users")
    public val totalEngagedUsers: Long? = null,
    public val models: List<Models>? = null,
    public val additional: JsonObject? = null,
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
      public val languages: List<Languages>? = null,
    ) {
      /**
       * Usage metrics for a given language for the given editor for Copilot code completions.
       */
      @Serializable
      public data class Languages(
        public val name: String? = null,
        @SerialName("total_engaged_users")
        public val totalEngagedUsers: Long? = null,
        @SerialName("total_code_suggestions")
        public val totalCodeSuggestions: Long? = null,
        @SerialName("total_code_acceptances")
        public val totalCodeAcceptances: Long? = null,
        @SerialName("total_code_lines_suggested")
        public val totalCodeLinesSuggested: Long? = null,
        @SerialName("total_code_lines_accepted")
        public val totalCodeLinesAccepted: Long? = null,
      )
    }

    public object Serializer : KSerializer<Editors> {
      override val descriptor: SerialDescriptor = generatedSerializer().descriptor

      override fun serialize(encoder: Encoder, `value`: Editors) {
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

      override fun deserialize(decoder: Decoder): Editors {
        val json = (decoder as JsonDecoder).json
        val element = decoder.decodeSerializableValue(JsonObject.serializer())
        val knownNames = setOf("name", "total_engaged_users", "models")
        val known = json.decodeFromJsonElement(generatedSerializer(), JsonObject(element.filterKeys { it in knownNames }))
        val additional = JsonObject(element - knownNames).ifEmpty { null }
        return known.copy(additional = additional)
      }
    }
  }

  /**
   * Usage metrics for a given language for the given editor for Copilot code completions.
   */
  @Serializable
  public data class Languages(
    public val name: String? = null,
    @SerialName("total_engaged_users")
    public val totalEngagedUsers: Long? = null,
  )

  public object Serializer : KSerializer<CopilotIdeCodeCompletions> {
    override val descriptor: SerialDescriptor = generatedSerializer().descriptor

    override fun serialize(encoder: Encoder, `value`: CopilotIdeCodeCompletions) {
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

    override fun deserialize(decoder: Decoder): CopilotIdeCodeCompletions {
      val json = (decoder as JsonDecoder).json
      val element = decoder.decodeSerializableValue(JsonObject.serializer())
      val knownNames = setOf("total_engaged_users", "languages", "editors")
      val known = json.decodeFromJsonElement(generatedSerializer(), JsonObject(element.filterKeys { it in knownNames }))
      val additional = JsonObject(element - knownNames).ifEmpty { null }
      return known.copy(additional = additional)
    }
  }
}
