package io.openai.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.List
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement

/**
 * Workflow metadata and state returned for the session.
 */
@Serializable
public data class ChatkitWorkflow(
  public val id: String,
  public val version: String?,
  @SerialName("state_variables")
  @Required
  public val stateVariables: List<StateVariables>? = null,
  public val tracing: ChatkitWorkflowTracing,
) {
  @Serializable(with = StateVariables.Serializer::class)
  public sealed interface StateVariables {
    @Serializable
    @JvmInline
    public value class CaseString(
      public val `value`: String,
    ) : StateVariables

    @Serializable
    @JvmInline
    public value class CaseLong(
      public val `value`: Long,
    ) : StateVariables

    @Serializable
    @JvmInline
    public value class CaseBoolean(
      public val `value`: Boolean,
    ) : StateVariables

    @Serializable
    @JvmInline
    public value class CaseDouble(
      public val `value`: Double,
    ) : StateVariables

    public object Serializer : KSerializer<StateVariables> {
      @OptIn(
        InternalSerializationApi::class,
        ExperimentalSerializationApi::class,
      )
      override val descriptor: SerialDescriptor =
          buildSerialDescriptor("io.openai.model.ChatkitWorkflow.StateVariables", PolymorphicKind.SEALED) {
        element("CaseString", String.serializer().descriptor)
        element("CaseLong", Long.serializer().descriptor)
        element("CaseBoolean", Boolean.serializer().descriptor)
        element("CaseDouble", Double.serializer().descriptor)
      }

      override fun deserialize(decoder: Decoder): StateVariables {
        val value = decoder.decodeSerializableValue(JsonElement.serializer())
        val json = requireNotNull(decoder as? JsonDecoder) { "Complex unions currently only supported for Json" }.json
        return json.attemptDeserialize(
          value,
          CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
          CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
          CaseBoolean::class to { CaseBoolean(decodeFromJsonElement(Boolean.serializer(), it)) },
          CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
        )
      }

      override fun serialize(encoder: Encoder, `value`: StateVariables) {
        when(value) {
          is CaseString -> encoder.encodeSerializableValue(String.serializer(), value.value)
          is CaseLong -> encoder.encodeSerializableValue(Long.serializer(), value.value)
          is CaseBoolean -> encoder.encodeSerializableValue(Boolean.serializer(), value.value)
          is CaseDouble -> encoder.encodeSerializableValue(Double.serializer(), value.value)
        }
      }
    }
  }
}
