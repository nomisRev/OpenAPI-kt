package io.openai.model

import kotlin.Boolean
import kotlin.Double
import kotlin.Long
import kotlin.OptIn
import kotlin.String
import kotlin.collections.Map
import kotlin.jvm.JvmInline
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
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
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull

/**
 * Workflow metadata and state returned for the session.
 */
@Serializable
public data class ChatkitWorkflow(
  public val id: String,
  public val version: String?,
  @SerialName("state_variables")
  public val stateVariables: Map<String, StateVariables>?,
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
        return when {
          value is JsonPrimitive && value.isString -> json.attemptDeserialize(
            value,
            CaseString::class to { CaseString(decodeFromJsonElement(String.serializer(), it)) },
          )
          value is JsonPrimitive && !value.isString && value.doubleOrNull != null -> json.attemptDeserialize(
            value,
            CaseLong::class to { CaseLong(decodeFromJsonElement(Long.serializer(), it)) },
            CaseDouble::class to { CaseDouble(decodeFromJsonElement(Double.serializer(), it)) },
          )
          value is JsonPrimitive && !value.isString && value.booleanOrNull != null -> json.attemptDeserialize(
            value,
            CaseBoolean::class to { CaseBoolean(decodeFromJsonElement(Boolean.serializer(), it)) },
          )
          else -> throw UnionSerializationException(value, emptyMap())
        }
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
